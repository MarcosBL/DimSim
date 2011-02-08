import os

import string

def searchPathByKeyword(baseDirectory, keyword):
    entries = []
    for root, dirs, files in os.walk(baseDirectory):
        for name in dirs:
            entry = os.path.join(root,name)
            if (string.find(entry, keyword) >= 0):
                entries.append(entry)
    return entries

class Data(object):
    name = ''
    value = ''
    def __init__(self, key, val):
        self.name = key
        self.value = val
            
class serialObject(object):
    itemlist = []
    def add(self,name, value):
        self.itemlist.append(Data(name,value))
    def clearBuffer(self):
        self.itemlist = []
    def exportData(self, path):
        fp = open(path,"w")
        for i in range(0, len(self.itemlist)):
            fp.write(str((self.itemlist[i]).name)+ '=' + str((self.itemlist[i]).value))
            if i < len(self.itemlist) - 1:
                fp.write(',')
        fp.close()
    def importData(self, path):
        fp = open(path,"r")
        line = fp.readline()
        if len(line) == 0:
            fp.close()
            return
        self.itemlist = []
        fileData = string.split(line,",")
        for i in range(0, len(fileData)):
            objectData = string.split(fileData[i],'=')
            self.itemlist.append(Data(objectData[0],objectData[1]))
        fp.close()
    def getData(self, index):
        return self.itemlist[index]
    def getSize(self):
        return len(self.itemlist)
    def get(self, key):
        value = ''
        for i in range(0, len(self.itemlist)):
            if (self.itemlist[i]).name == key:
                value = (self.itemlist[i]).value
                break
        return value
    def set(self, key, val):
        bFound = False
        for i in range(0, len(self.itemlist)):
            if (self.itemlist[i]).name == key:
                (self.itemlist[i]).value = val
                bFound = True
                break
        if (bFound == False):
            self.itemlist.append(Data(key,val))