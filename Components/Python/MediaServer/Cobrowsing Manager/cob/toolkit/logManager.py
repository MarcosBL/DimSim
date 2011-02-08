import string
import logging

def setupLogging(path, level = None):
    engine = logging.getLogger('Synchrolive')
    logHandler = logging.FileHandler(path)
    logFormatter = logging.Formatter('%(asctime)s %(levelname)s :: %(message)s')
    logHandler.setFormatter(logFormatter)
    engine.addHandler(logHandler)
    
    if not level:
        level = 'info'
        
    if string.lower(level) == 'debug':
        engine.setLevel(logging.DEBUG)
    elif string.lower(level) == 'warn':
        engine.setLevel(logging.WARN)
    elif string.lower(level) == 'info':
        engine.setLevel(logging.INFO)
    else:
        engine.setLevel(logging.ERROR)
        
    return
        
def log(message, level = None):
    engine = logging.getLogger('Synchrolive')
    if not level:
        level = logging.INFO
    engine.log(level, message)
    return