

// Header for NolClientApi
#pragma once

#ifdef __cplusplus 
#define DLLEXPORT extern "C" __declspec(dllexport)
#else
#define DLLEXPORT __declspec(dllexport)
#endif

#ifdef __cplusplus 
#define DLLIMPORT extern "C" __declspec(dllimport)
#else
#define DLLIMPORT __declspec(dllimport)
#endif


namespace NOL3Client 
{
typedef int ResultCode; 


typedef enum { Undef = -1, NewOrder, ModOrder,DelOrder,StatOrder} OrderType;

typedef enum { UndefList = -1, All,Symbol,ISIN,CFI,MarketCode} TypeofList;

 typedef struct _NolBidAskTbl_
 {
      int depth; // pozycja ofery 1-5 
      int side;  // 1- kupno/ 2- sprzeda¿
      double price; // cena ofery 
      int size;     // rozmiar oferty
	  int amount;		// iloœæ ofert
 }NolBidAskTbl;

typedef struct _NolBidAskStr_
{
         int offersize;              
         NolBidAskTbl* bidask_table;
}NolBidAskStr;
 


// structure contatining information about Ticker name and isin
typedef struct _NolTicker_
{
char Isin[13];		// International Securities Identifying Number
char Name[21];		// Full name of the ticker
char MarketCode[3]; 
char CFI[7];
char Group[3];
}NolTicker;

	// structure containing information about list of tickers
typedef struct  _NolTickers_
{
 NolTicker* ptrtickerslist;	// pointer to list of tickers
int size;			// size of table of ticker structures
} NolTickers;

	// structure containing information about current data
typedef struct _NolRecentInfo_
{
int BitMask; 		// mask of data
NolTicker ticker;	// structure containing information about name and isin 
double ValoLT;	// a(flag in BitMask) - value of last transaction 1
int VoLT;		// b - volume of last transaction 2
char ToLT[9];		// c - time of last transaction 4
double Open;		// d - open price 8
double High;		// e - current high price 10
double Low;		// f - current low price 20
double Close;		// g - close price 40
double Bid;		// h - the best bid price 80
double Ask;		// i - the best ask price  100
int BidSize;		// j - size of the best bid 200
int AskSize;		// k - size of the best ask 400
int TotalVolume;	// l - total volume 800
double TotalValue;	// m - total value  1000
int OpenInterest;	// n - number of open intrests 2000
char Phase[5];			// o - ticker phase 4000
char Status[3];		// p - ticker status 8000
int BidAmount;		// r - amount of bid 10000
int AskAmount;		// s - amount of ask 20000
double OpenValue;	// t - open value    40000
double CloseValue;	// u - close value   80000
double ReferPrice;// w - reference price 100000 
NolBidAskStr offers; // y - offers       200000
									  
int Error;			// z - error         
} NolRecentInfo;


typedef struct _NolFund_ 
{
	char name[30];         // name of fundation
	char value[30];        // value of fundation
}NolFund;

typedef struct _NolPos_ 
{
	NolTicker ticker;         // isin of acctivity
	int acc110; 	       // amount of acctivity
	int acc120;
}NolPos; 

typedef struct _NolStatement_ 
{
	char name[13];              // account
	char ike[2];				// is ike ?
	char type[2];				// type of account
	NolFund* ptrfund;           // pointer to table of fundations
	int sizefund;               // size of fountations table
	NolPos* ptrpos;             // pointer to table of acctivities
	int sizepos;                // size of acctivities table
}NolStatement; 

typedef struct _NolAggrStatement_ 
{
	NolStatement* ptrstate;       // pointer to nolstatments
	int size;                     // number of accounts
}NolAggrStatement; 

typedef struct _NolOrderReport_
{
	long long BitMask;		// BitMask wich data are active
	int ID;				// bit 0 ID from Library				0001
	char OrdID[10]; 		// bit 1 of BitMask  order ID		0002
	char OrdID2[10]; 		// bit 2 secondary order ID			0004
	char StatReqID[10];		// bit 3 ID of OrderStatusRequest	0008
	char ExecID[10];		// bit 4 transaction ID				0010
	char ExecTyp;			// bit 5 type of execution			0020
	char Stat;				// bit 6 order satus				0040
	int RejRsn;				// bit 7 reject reason				0080
	char Acct[17];			// bit 8 account					0100
	NolTicker ticker; 		// bit 9 name/alias ; 10 - isin		0200 0400
	char Side[2];			// bit 11							0x0800
	int Qty;				// bit 12 Quantity order			0x1000
	char OrdTyp[2];			// bit 13 order type			0x2000
	float Px;				// bit 14 price						0x4000
	float StopPx;			// bit 15 stop price				0x8000
	char Ccy[6];			// bit 16 currency										 0x10000
	char TmInForce[2];		// bit 17 specifies how long the order remains in effect 0x20000
	char ExpireDt[9];		// bit 18 date of order expiration						 0x40000		
	float LastPx;			// bit 19 price of this last fill						 0x80000
	int LastQty;			// bit 20 Quantity bought/sold on this last fill		0x100000
	int LeavesQty;		    // bit 21 Quantity open for further execution				0x200000
	int CumQty;			    // bit 22 Total Quantity									0x400000
	char TxnTm[18];			// bit 23 time of execution/order creation				0x800000
	float Comm;				// bit 24 Commission										0x1000000
	float NetMny;			// bit 25 Total amount due as the result of the transaction	0x2000000
	int MinQty;			// bit 26 Minimum quantity of an order to be executed			0x4000000
	int DisplayQty;		// bit 27 the quantity to be displayed							0x8000000
	float TrgrPx;			// bit 28 the price at which the triegger should hit		0x10000000	
	char DefPayTyp[2];		// bit 29 Defferred Payment Type							0x20000000
	char BizRejRsn;			// bit 30													0x40000000
	char Txt[160];			// bit 31													0x80000000
	char ExpireTm[9];		// bit 32 expire time									   0x100000000	
} NolOrderReport;

typedef struct _NolOrderRequest_
{
	int BitMask;
	char OrigID[10];	//a	secondary order ID from library		0001
	char OrdID[10];	//b	order ID								0002
	char OrdID2[10];	//c	secondary order ID					0004
	char Acct[17];	//d	account										0008							
	int MinQty;	//e	minimum quantity of an order to be executed	0010	
	int DisplayQty;	//f	the quantity to be displayed			0020
	NolTicker ticker;	//g, h ticker structure					    0040 - isin,0080 - name/alias
	char Side[2];	//i													0100
	int Qty;		//j	quantity ordered							0200
	char OrdTyp[2];	//k	order type									0400
	float Px;		//l	price										0800
	float StopPx;	//m	stop price									1000
	char Ccy[6];	//n	curenccy									2000
	char TmInForce[2];	//o	specifies how long the order remains in effect 4000
	char ExpireDt[9];	//p	date of order expiration				   8000	
	float TrgrPx;	//r	the price at which the trigger should hit	   10000
	char DefPayTyp[2];	//s 	Defferred Payment Type					   20000
	char SessionDt[9];	//t	 session date								40000
	char ExpireTm[9];	//u expire time									80000
} NolOrderRequest;

	// initialize function 
	// AppId - string containing the application information  
DLLEXPORT ResultCode __stdcall Initialize(const char* AppId);

// function for adding ticker(s) to a filter
// TickersToAdd - string containing isins separated by ';'
// Flush = true, short/name ticker, Flush = false - ISIN
DLLEXPORT ResultCode __stdcall AddToFilter
(
	const char* TickersToAdd,
	bool Flush = false
);

// function for removing ticker(s) from a filter
// TickersToRem - string containing isins separated by ';', 
// Flush = true, short/name ticker, Flush = false - ISIN 
DLLEXPORT ResultCode __stdcall RemFromFilter
(
 const char* TickersToRem,
 bool Flush = false
);

// function for setting a callback function 
// void (*ptrcallback) (RecentInfo*) - pointer to callback function 
DLLEXPORT ResultCode __stdcall SetCallback
(
	void ( *ptrcallback) (NolRecentInfo*)
);
// function for clearing a filter
// Flush - flag used for automatic filter approvment
DLLEXPORT ResultCode __stdcall ClearFilter();

// function for describing errors 
DLLEXPORT const char* __stdcall GetResultCodeDesc
(
 ResultCode code 	/* code returned by function */
);

// function for getting the information about verson of dll
DLLEXPORT const char* __stdcall Get_Version(void);

// shutdown function
DLLEXPORT ResultCode __stdcall Shutdown(void);

// function for setting a callback function (accounts information)
DLLEXPORT ResultCode __stdcall SetCallbackAccount(void (*ptrcallbackaccount)(NolAggrStatement*)) ;

// function for setting a callback function (orders information)
DLLEXPORT ResultCode __stdcall SetCallbackOrder(void (*ptrcallbackorder)(NolOrderReport*));

// function for setting a callback function (outlook information)
DLLEXPORT ResultCode __stdcall SetCallbackOutlook(void (*ptrcallbackoutlook)(const char*)); 

// function for orders ( modifications, cancels, status request)
DLLEXPORT ResultCode __stdcall APIOrderRequest(NolOrderRequest*, NolOrderReport* ,OrderType Typ);  

DLLEXPORT ResultCode __stdcall SetTradingSess(bool val);

DLLEXPORT ResultCode __stdcall SetCallbackDelay(void (*ptrcallbackdelay)(float delay));

DLLEXPORT ResultCode __stdcall GetTickers( NolTickers* ptrtickers,TypeofList Typ=UndefList,NOL3Client::NolTicker* in_ticker=NULL);

// function for instantiante an Tickers's object 
DLLEXPORT  NolTickers* __stdcall InitListTickers(void);

DLLEXPORT  ResultCode __stdcall ReleaseTickersList( NOL3Client::NolTickers* ptrtickers );

DLLEXPORT  ResultCode __stdcall SetCallbackStatus(void ( *ptr)( int ) );
}