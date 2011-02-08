import random

def gen():
	idlist = []
	for i in range(0,4):
		idlist.append(random.choice('abcdefghijklmnopqrstuvwxyz'))
	return ''.join(idlist) + str(random.randrange(1111,9999))
