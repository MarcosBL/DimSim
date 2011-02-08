import string

class jsonObject(object):
	itemlist = []
	def add(self,name, value):
		self.itemlist.extend([name,value])
	def clearResponse(self):
		self.itemlist = []
	def jsonResponse(self):
		response = '{'
		for i in range(0,len(self.itemlist) - 1,2):
			response += str(self.itemlist[i])
			if string.find(str(self.itemlist[i+1]),'[{') >= 0:
			    response += ':' + str(self.itemlist[i+1])
			else:
			     response += ':"' + str(self.itemlist[i+1]) + '"'
			if i < (len(self.itemlist) - 2):
				response += ','
		response += '}'
		return response
