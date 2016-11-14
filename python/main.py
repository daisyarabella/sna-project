import pandas as pd
import numpy as np
import csv
import math

def main():
	
	data = pd.read_csv('../data/linearEqs.csv')
	print(data)
	print('\n')

	# Get all of the 'solutions' to linear equations in matrix B
	matrixB = data['S(t+1)']

	# Put the coefficient data of all linear equations in matrix A
	matrixA = data['aCo']
	matrixA = matrixA.to_frame()
	matrixA['bCo'] = data['bCo']
	matrixA['cCo'] = data['cCo']

	# Solve system of linear equations and print values for a, b and c
	a, b, c = np.linalg.lstsq(matrixA.as_matrix(), matrixB.as_matrix())[0]
	print('a = ' + str(a))
	print('b = ' + str(b))
	print('c = ' + str(c))

	#m = (-b-math.sqrt((b*b)-4*a*c))/(2*a)
	# Set m as the total number of adopters
	m = data.tail(1)['bCo'].values
	print('m = ' + str(m))
	
	# Calculate and print p and q
	p = a/m	
	q = b + p
	print('\np = a/m = '+str(p))
	print('q = b+p = '+str(q))

	#why is be sometimes <1 and >1
	# is p working?
	#what is c for
	# is m no nodes or is it quadratic

if __name__ == "__main__":
	main()
