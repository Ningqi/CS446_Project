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

# this program samples the funny reviews and not so funny reviews,
# since there is a large inbalance of number of reviews we use sample size of 
# SAMPLE set below, this would be adjusted to the number of funny reviews
# thus in total there would be a SAMPLE funny reviews and SAMPLE not funny reviews
# total of 2*SAMPLE reviews. From those we create training and testing datasets of
# equal size. The size of the training depends of the cross validation parameter
# which is set to 5, but can be changed. Thus there are five chunks of (2*SAMPLE)/5.
SAMPLE=12000
HOLD=5000
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

def cross_validation(args):
	if len(args) < 2:
		print "inputformat: <funny_reviews> <not_funny_reviews>"
		return 0
	
	path_to_cv_output = "data/cv/"
	folds = 5
	funny=args[0]
	notfunny=args[1]

	init_funny = []
	init_notfunny = []

	with open(funny) as f:
		hold_the_sauce = []
		held_out_set = []
		funny_conts = f.readlines()
		hold_the_sauce  = random.sample(range(len(funny_conts)), HOLD)
		for i, item in enumerate(funny_conts):
			if i in hold_the_sauce:
				held_out_set.append(str(i) + "," + item)

		funny_conts = list( set(funny_conts) - set(held_out_set) )
		init_funny = random.sample(funny_conts, SAMPLE )

		with open(path_to_cv_output + "hold_out_set.txt", 'w') as orderUp:
			orderUp.writelines(held_out_set)
	
	with open(notfunny) as f:
		notfunny_conts = f.readlines()
		init_notfunny = random.sample(notfunny_conts, SAMPLE )

	sampleSize = (int) (len(init_funny) * (1.0/(1.0*folds)))
	
	funny_training = path_to_cv_output+"funny_training_sample_"
	notfunny_training= path_to_cv_output+"notfunny_training_sample_"
	funny_testing = path_to_cv_output+"funny_testing_sample_"
	notfunny_testing = path_to_cv_output+"notfunny_testing_sample_"
	suffix = ".txt"

	i = 0
	for funny_t, funny_v in k_fold_cross_validation(init_funny, folds):
		i = i + 1
		with open(funny_training + str(i) + suffix, 'w') as f:
			f.writelines(funny_t)
			
		with open(funny_testing + str(i) + suffix, 'w') as f:
			f.writelines(funny_v)

	i = 0
	for notfunny_t, notfunny_v in k_fold_cross_validation(init_notfunny, folds):
		i = i + 1
		with open(notfunny_training + str(i) + suffix, 'w') as f:
			f.writelines(notfunny_t)
			
		with open(notfunny_testing + str(i) + suffix, 'w') as f:
			f.writelines(notfunny_v)
	
def k_fold_cross_validation(items, k, randomize=False):
	if randomize:
		items = list(items)
		random.shuffle(items)

	slices = [items[i::k] for i in xrange(k)]

	for i in xrange(k):
		validation = slices[i]
		training = [item for s in slices if s is not validation for item in s]
		yield training, validation	
		

if __name__ == '__main__':
	#main(sys.argv[1:])
	cross_validation(sys.argv[1:])

