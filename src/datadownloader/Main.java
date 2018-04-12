package datadownloader;

import bossaAPIpackage.BossaAPI;

import java.util.Scanner;

/** Simple example of JNA interface mapping and usage. */
@SuppressWarnings("Convert2Lambda")
public class Main {


    public static void main(String[] args) {


        int response = BossaAPI.SetCallbackStatus(
                var -> System.out.println("SetCallbackStatus of callback function: " + var));

        System.out.println("SetCallbackStatus returned: " + BossaAPI.GetResultCodeDesc(response));

        response = BossaAPI.SetCallbackAccount(new BossaAPI.SetCallbackAccountDummyAPI() {
            @Override
            public void invoke(BossaAPI.NolAggrStatementAPI nolaggrstatementAPI) {
                System.out.println("SetCallbackAccount of callback function: ");
                System.out.println("Number of accounts: " + nolaggrstatementAPI.getStatement().size());
                System.out.println("Account name: " + nolaggrstatementAPI.getStatement().get(0).getName());
                System.out.println("IKE status: " + nolaggrstatementAPI.getStatement().get(0).getIke());
                System.out.println("Account type: " + nolaggrstatementAPI.getStatement().get(0).getType());
                System.out.println("Fund size: " + nolaggrstatementAPI.getStatement().get(0).getFund().size());
                System.out.println("Activity size: " + nolaggrstatementAPI.getStatement().get(0).getPositions().size());
                System.out.println("Fund name: " + nolaggrstatementAPI.getStatement().get(0).getFund().get(0).getName());
                System.out.println("Fund value: " + nolaggrstatementAPI.getStatement().get(0).getFund().get(0).getValue());
                //System.out.println("Amount of activity: " + nolaggrstatement.ptrstate.ptrpos.acc110);
            }
        });

        System.out.println("SetCallbackAccount returned: " + BossaAPI.GetResultCodeDesc(response));

        response = BossaAPI.SetCallbackDelay(
                delay -> System.out.println("SetCallbackDelay of callback function: " + delay));

        System.out.println("SetCallbackDelay returned: " + BossaAPI.GetResultCodeDesc(response));

        response = BossaAPI.SetCallbackOrder(
                nolorderreport -> System.out.println("SetCallbackOrder of callback function: " + nolorderreport));

        System.out.println("SetCallbackOrder returned: " + BossaAPI.GetResultCodeDesc(response));

        response = BossaAPI.SetCallbackOutlook(
                outlook -> System.out.println("SetCallbackOutlook of callback function: " + outlook));

        System.out.println("SetCallbackOutlook returned: " + BossaAPI.GetResultCodeDesc(response));

        response = BossaAPI.Initialize();

        System.out.println("Initialize returned: " + BossaAPI.GetResultCodeDesc(response));

        response = BossaAPI.SetCallback(
                new BossaAPI.SetCallbackDummyAPI() {
                    @Override
                    public void invoke(BossaAPI.NolRecentInfoAPI nolrecentinfo) {
                        System.out.println("SetCallback of callback function: " + nolrecentinfo);
                        //int offerSize = nolrecentinfo.getOffers().getBidask_table().size(); //TODO integrate getoffers and getbidasktable
                        System.out.println(" SetCallback bid ask: " + nolrecentinfo.getOffers().getBidask_table().get(0));
                    }
                }
        );
        System.out.println("SetCallback returned: " + BossaAPI.GetResultCodeDesc(response));
        System.out.println(BossaAPI.Get_Version());
        //#TODO api działa - teraz należy przetestować każdą funkcję z osobna i napisać ściągacz danych

/*        BossaAPIInterface api = BossaAPI.getApiInstance();
        BossaAPIInterface.NolTickers tckrs= api.InitListTickers();
        api.GetTickers(tckrs,TypeofList.All,null);
        System.out.println("ticker list size: " + tckrs.size);*/

        //BossaAPI.NolTickersAPI tickers = BossaAPI.InitListTickers();
        //System.out.println("api ticker list code: " + BossaAPI.GetTickers(tickers, TypeofList.All,null));
        //BossaAPI.GetTickers(TypeofList.All,null);

/*        System.out.println("Number of tickers: " + tickers.getTickersList().size());
        String isin = tickers.getTickersList().get(100).getIsin();//new String(NolTicker.class.cast(tickers.ptrtickerslist.toArray(tickers.size)[100]).Isin);
        String isin2 = tickers.getTickersList().get(10).getIsin();
        System.out.println("ticker: " + isin + " ticker2: " + isin2);
        BossaAPI.ClearFilter();
        String isins = isin2+ ";" + isin + ";";
        System.out.println(BossaAPI.GetResultCodeDesc(BossaAPI.AddToFilter(isins, false)));
        System.out.println(BossaAPI.GetResultCodeDesc(BossaAPI.RemFromFilter(isin2, false)));
        BossaAPI.ClearFilter();
        System.out.println(BossaAPI.GetResultCodeDesc(BossaAPI.AddToFilter(isin, false)));*/
        Scanner keyboard = new Scanner(System.in);
        System.out.println("enter an integer");
        @SuppressWarnings("unused") int myint = keyboard.nextInt();
    }
}