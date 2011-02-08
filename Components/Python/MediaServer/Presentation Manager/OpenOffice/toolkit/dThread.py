import sys
import threading

class dThread(threading.Thread):
    def __init__(self, *args, **keywords):
        threading.Thread.__init__(self, *args, **keywords)
        self.killed = False

    def start(self):
        self.__run_backup = self.run
        self.run = self.__run
        threading.Thread.start(self)

    def __run(self):
        sys.settrace(self.globaltrace)
        self.__run_backup()
        self.run = self.__run_backup

    def globaltrace(self, frame, reason, arg):
        if reason == 'call':
            return self.localtrace
        else:
            return None

    def localtrace(self, frame, reason, arg):
        if self.killed:
            if reason == 'line':
                self.__stopped = True
                raise SystemExit()
        return self.localtrace

    def kill(self):
        self.killed = True