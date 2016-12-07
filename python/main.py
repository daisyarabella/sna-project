import pandas as pd
import numpy as np
import csv
import math

def main():
	
	# Get and read in data 
	data = pd.read_csv('../data/linearEqs.csv')
	print(data)
	print('\n')

	# Get all of the 'solutions' to linear equations in solution matrix
	solutions = data['S(t+1)']

	# Put the coefficient data of all linear equations in coefficient matrix
	coeff = data['aCo']
	coeff = coeff.to_frame()
	coeff['bCo'] = data['bCo']
	coeff['cCo'] = data['cCo']

	# Solve system of linear equations and print values for a, b and c
	a, b, c = np.linalg.lstsq(coeff.as_matrix(), solutions.as_matrix())[0]
	print('a = ' + str(a))
	print('b = ' + str(b))
	print('c = ' + str(c)+'\n')

	# Find p and q based on mMinus
	mMinus = (-b-math.sqrt((b*b)-4*a*c))/(2*a)
	print('m = (-b-sqrt(b^2-4ac))/2a = ' + str(mMinus))
	pMinus = a/mMinus
	qMinus = b + pMinus
	print('p- = a/m = '+str(pMinus))
	print('q- = b+p = '+str(qMinus)+'\n')

	# Find p and q based on mPlus
	mPlus = (-b+math.sqrt((b*b)-4*a*c))/(2*a)
	print('m = (-b+sqrt(b^2-4ac))/2a = ' + str(mPlus))
	pPlus = a/mPlus	
	qPlus = b + pPlus
	print('p+ = a/m = '+str(pPlus))
	print('q+ = b+p = '+str(qPlus)+'\n')

	# Set m as the total number of adopters
	#m = data.tail(1)['bCo'].values

if __name__ == "__main__":
	main()
