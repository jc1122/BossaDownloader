package datadownloader;

import gui.Controller;
import gui.Model;

/** Simple example of JNA interface mapping and usage. */
@SuppressWarnings("Convert2Lambda")
public class Main {


    public static void main(String[] args) throws Exception {

        System.out.println("startAPI");
        Model model = new Model();
        Controller controller = new Controller(model);

 /*       //System.out.println(BossaAPI.Initialize());
        String response = BossaAPI.SetCallbackStatus(
                var -> System.out.println("SetCallbackStatus of callback function: " + var));

        System.out.println("SetCallbackStatus returned: " + response);

        response = BossaAPI.SetCallbackAccount(new BossaAPI.SetCallbackAccountDummyAPI() {
            @Override
            public void invoke(BossaAPI.NolAggrStatementAPI nolaggrstatementAPI) {
                int account = 3;
                System.out.println("SetCallbackAccount of callback function: ");
                System.out.println("Number of accounts: " + nolaggrstatementAPI.getStatements().size());
                System.out.println("Account name: " + nolaggrstatementAPI.getStatements().get(account).getName());
                System.out.println("IKE status: " + nolaggrstatementAPI.getStatements().get(account).getIke());
                System.out.println("Account type: " + nolaggrstatementAPI.getStatements().get(account).getType());
                //System.out.println("Fund size: " + nolaggrstatementAPI.getStatements().get(account).getFundList().size());
                for (Map.Entry<String, Double> fund : nolaggrstatementAPI.getStatements().get(account).getFundMap().entrySet()) {
                    System.out.println("Fund name: " + fund.getKey());
                    System.out.println("Fund value: " + fund.getValue());
                }

                System.out.println("Number of positions: " + nolaggrstatementAPI.getStatements().get(account).getPositions().size());
                System.out.println("Amount of activity: " + nolaggrstatementAPI.getStatements().get(account).getPositions().get(0).getAcc110());
                System.out.println("Amount of blocked activity: " + nolaggrstatementAPI.getStatements().get(account).getPositions().get(0).getAcc120());
            }
        });

        System.out.println("SetCallbackAccount returned: " + response);

        response = BossaAPI.SetCallbackDelay(
                delay -> System.out.println("SetCallbackDelay of callback function: " + delay));

        System.out.println("SetCallbackDelay returned: " + response);

        response = BossaAPI.SetCallbackOrder(
                nolorderreport -> System.out.println("SetCallbackOrder of callback function: " + nolorderreport));

        System.out.println("SetCallbackOrder returned: " + response);

        response = BossaAPI.SetCallbackOutlook(
                outlook -> System.out.println("SetCallbackOutlook of callback function: " + outlook));

        System.out.println("SetCallbackOutlook returned: " + response);

        String resp = BossaAPI.Initialize();
        System.out.println(BossaAPI.SetTradingSess(true));
        System.out.println("Initialize returned: " + resp);


        System.out.println(BossaAPI.Get_Version());


        BossaAPI.NolTickersAPI tickers = BossaAPI.GetTickers(TypeofList.All, null);

        System.out.println("Number of tickers: " + tickers.getTickersList().size());
        //String isin = tickers.getTickersList().get(100).getIsin();//new String(NolTicker.class.cast(tickers.ptrtickerslist.toArray(tickers.size)[100]).Isin);
        BossaAPI.NolTickerAPI tmpticker = tickers.getTickersList().get(100);
        System.out.println(tmpticker.getIsin());
        BossaAPI.NolTickersAPI tickers2 = BossaAPI.GetTickers(TypeofList.All, tmpticker);
        System.out.println("Number of tickers: " + tickers2.getTickersList().size());
        //pole w tickerze nie moze byc puste
        String isin = tickers.getTickersList().get(100).getIsin();
        String isin2 = tickers.getTickersList().get(10).getIsin();
        String name = tickers.getTickersList().get(100).getName();
        String name2 = tickers.getTickersList().get(10).getName();
        String code = tickers.getTickersList().get(100).getMarketCode();
        String code2 = tickers.getTickersList().get(10).getMarketCode();
        String cfi = tickers.getTickersList().get(100).getCFI();
        String cfi2 = tickers.getTickersList().get(10).getCFI();
        String group = tickers.getTickersList().get(100).getGroup();
        String group2 = tickers.getTickersList().get(10).getGroup();
        System.out.println("isin: " + isin + " isin2: " + isin2);
        System.out.println("name: " + name + " name2: " + name2);
        System.out.println("code: " + code + " code2: " + code2);
        System.out.println("cfi: " + cfi + " cfi2: " + cfi2);
        System.out.println("group: " + group + " group2: " + group2);

        String isins = isin + ";" + isin2;
        System.out.println("isins: " + isins);
        System.out.println("names: " + name + ";" + name2);

        BossaAPI.SetCallback(
                new BossaAPI.SetCallbackDummyAPI() {
                    @Override
                    public void invoke(BossaAPI.NolRecentInfoAPI nolrecentinfo) {
                        System.out.println("SetCallback of callback function: " + nolrecentinfo);
                        //int offerSize = nolrecentinfo.getOffers().getBidask_table().size();
                        System.out.println(" ISIN " + nolrecentinfo.getTicker().getIsin());
                        System.out.println(" phase: " + nolrecentinfo.getPhase());
                        System.out.println(" status: " + nolrecentinfo.getStatus());
                        System.out.println(" ToLT: " + nolrecentinfo.getToLT());
                        System.out.println(" ask: " + nolrecentinfo.getAsk());
                        System.out.println(" ask amount: " + nolrecentinfo.getAskAmount());
                        System.out.println(" ask size: " + nolrecentinfo.getAskSize());
                        System.out.println(" bid: " + nolrecentinfo.getBid());
                        System.out.println(" bid amount: " + nolrecentinfo.getBidAmount());
                        System.out.println(" bid size: " + nolrecentinfo.getBidSize());
                        System.out.println(" bitmask: " + Integer.toBinaryString(nolrecentinfo.getBitMask()));
                        System.out.println(" close: " + nolrecentinfo.getClose());
                        System.out.println(" close value: " + nolrecentinfo.getCloseValue());
                        System.out.println(" error: " + nolrecentinfo.getError());
                        System.out.println(" high: " + nolrecentinfo.getHigh());
                        System.out.println(" low: " + nolrecentinfo.getLow());
                        System.out.println(" offers: " + nolrecentinfo.getOffers());
                        System.out.println(" offers size: " + nolrecentinfo.getOffers().size());
                        System.out.println(" offers 0 price: " + nolrecentinfo.getOffers().get(0).getPrice());
                        System.out.println(" offers 0 amount: " + nolrecentinfo.getOffers().get(0).getAmount());
                        System.out.println(" offers 0 depth: " + nolrecentinfo.getOffers().get(0).getDepth());
                        System.out.println(" offers 0 side: " + nolrecentinfo.getOffers().get(0).getSide());
                        System.out.println(" offers 0 size: " + nolrecentinfo.getOffers().get(0).getSize());
                        System.out.println(" offers 1 price: " + nolrecentinfo.getOffers().get(1).getPrice());
                        System.out.println(" offers 1 amount: " + nolrecentinfo.getOffers().get(1).getAmount());
                        System.out.println(" offers 1 depth: " + nolrecentinfo.getOffers().get(1).getDepth());
                        System.out.println(" offers 1 side: " + nolrecentinfo.getOffers().get(1).getSide());
                        System.out.println(" offers 1 size: " + nolrecentinfo.getOffers().get(1).getSize());
                        System.out.println(" open: " + nolrecentinfo.getOpen());
                        System.out.println(" open interest: " + nolrecentinfo.getOpenInterest());
                        System.out.println(" open value: " + nolrecentinfo.getOpenValue());
                        System.out.println(" reference price: " + nolrecentinfo.getReferPrice());
                        System.out.println(" total value: " + nolrecentinfo.getTotalValue());
                        System.out.println(" total volume: " + nolrecentinfo.getTotalVolume());
                        System.out.println(" valo lt: " + nolrecentinfo.getValoLT());
                        System.out.println(" volt: " + nolrecentinfo.getVoLT());
                    }
                }
        );

        for (int i = 0; i < 1; i++)
            System.out.println(BossaAPI.AddToFilter(isin, true));
        //BossaAPI.Shutdown();
        System.out.println("SetCallback returned: " + response);
        Scanner keyboard = new Scanner(System.in);
        System.out.println("enter an integer");
        @SuppressWarnings("unused") int myint = keyboard.nextInt();*/
    }
}