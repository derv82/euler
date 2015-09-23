#!/usr/bin/python

from math import sqrt

def get_period(n):
	s = sqrt(n)
	result = []
	temp = s
	for i in xrange(0, 17):
		next_fract = int(temp)
		result.append(next_fract)
		temp = 1 / (temp - int(temp))
	result = result[:find_repeats(result[1:])]
	print "sqrt(%d)=[%d;(%s)]" % (n, result[0], ','.join(str(x) for x in result[1:]))
	return result[1:]

def find_repeats(l):
	for i in xrange(1, len(l) / 2):
		if equal(l[:i], l[i:i+i]):
			if is_repeating(l, l[:i]):
				return i + 1
	return -1

def is_repeating(l, subl):
	for i, value in enumerate(l):
		if value != subl[i % len(subl)]: return False
		if i > len(subl) * 3: break
	return True
		
def equal(one, two):
	for i, value in enumerate(one):
		if i >= len(two): break
		if value != two[i]: return False
	return True
	

count = 0
n = 1
ncount = 0
while True:
	n += 1
	if sqrt(n) == int(sqrt(n)): continue
	ncount += 1
	if ncount > 10000: break
	if len(get_period(n)) % 2 != 0:
		count += 1
print count

