// test.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "nolclientapi.h"
#include  <iostream>

using namespace std;

void print_status(NOL3Client::NolAggrStatement* statement) {
	cout << " value: ";
	cout << (int)statement->ptrstate->type[0];
	cout << '\n';
	cout << statement->ptrstate->type[0];
	cout << '\n';
	cout << statement->ptrstate->type[1];
	cout << '\n';
	for (int i = 0; i < 13; i++) {
		cout << (int)statement->ptrstate->name[i] << " " << statement->ptrstate->name[i];
		cout << '\n';
	}
	cout << statement->size;
};

int _tmain(int argc, _TCHAR* argv[])
{
	cout << NOL3Client::GetResultCodeDesc(NOL3Client::Initialize("BOS;BOS"));
	cout << NOL3Client::SetCallbackAccount(print_status);
	cin.get();

	return 0;
}

