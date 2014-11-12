#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
#  sampleData.py
#  
#  Copyright 2014 squirtle <squirtle@squirtle-ThinkPad-X240>
#  
#  This program is free software; you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation; either version 2 of the License, or
#  (at your option) any later version.
#  
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#  
#  You should have received a copy of the GNU General Public License
#  along with this program; if not, write to the Free Software
#  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
#  MA 02110-1301, USA.
#  
#  

import sys
import random

def main(args):
	if len(args) < 1:
		print "Need at least one arguement"
		return 0
		
	fraction = 0.1
	outputfile = "output_sample.txt"
	outputdisj = "output_cleaned.txt"
	print "arguments:"
	print args
	lines = []
	with open(args[0]) as myFile:
		lines = myFile.readlines()

	print len(lines)
	
	sampleSize = (int) ( len(lines) * fraction ) 
	print sampleSize
	
	sampleFiles = random.sample(lines, sampleSize)
	print sampleFiles[0]
	
	with open(outputfile, 'w') as f:
		f.writelines(sampleFiles)
	
	leftover = list( set(lines) - set(sampleFiles) )
	
	with open(outputdisj, 'w') as f:
		f.writelines(leftover)
	
	
	return 0

if __name__ == '__main__':
	main(sys.argv[1:])

