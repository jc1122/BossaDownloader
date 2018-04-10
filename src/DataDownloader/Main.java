package datadownloader;

import datadownloader.BossaAPIInterface.*;

import java.util.Scanner;

/** Simple example of JNA interface mapping and usage. */
@SuppressWarnings("Convert2Lambda")
public class Main {


    public static void main(String[] args) {

        BossaAPIInterface api = BossaAPI.getApiInstance();
        int response = api.SetCallbackStatus(
                new BossaAPIInterface.SetCallbackStatusDummy() {
                    public void invoke(int var) {
                    System.out.println("SetCallbackStatus of callback function: " + var);
            }
        });

/*        int response = BossaAPI.SetCallbackStatus(
                var -> System.out.println("SetCallbackStatus of callback function: " + var));*/

        System.out.println("SetCallbackStatus returned: " + api.GetResultCodeDesc(response));

 /*       response = BossaAPI.SetCallbackAccount(new BossaAPI.SetCallbackAccountDummyAPI() {
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
        });*/
        response = api.SetCallbackAccount(
                new BossaAPIInterface.SetCallbackAccountDummy() {
                    @Override
                    public void invoke(NolAggrStatement nolaggrstatement) {
                        System.out.println("SetCallbackAccount of callback function: ");
                        System.out.println("Number of accounts: " + nolaggrstatement.size);
                        System.out.println("Account name: " + new String(nolaggrstatement.ptrstate.name));
                        System.out.println("IKE status: " + new String(nolaggrstatement.ptrstate.ike));
                        System.out.println("Account type: " + new String(nolaggrstatement.ptrstate.type));
                        System.out.println("Fund size: " + nolaggrstatement.ptrstate.ptrfund);
                        System.out.println("Activity size: " + nolaggrstatement.ptrstate.sizepos);
                        System.out.println("Fund name: " + new String(nolaggrstatement.ptrstate.ptrfund.name));
                        System.out.println("Fund value: " + new String(nolaggrstatement.ptrstate.ptrfund.value));
                        //System.out.println("Amount of activity: " + nolaggrstatement.ptrstate.ptrpos.acc110);
                    }
                }
        );
        System.out.println("SetCallbackAccount returned: " + api.GetResultCodeDesc(response));

/*        response = BossaAPI.SetCallbackDelay(
                delay -> System.out.println("SetCallbackDelay of callback function: " + delay));*/
        response = api.SetCallbackDelay(
                new SetCallbackDelayDummy() {
                    @Override
                    public void invoke(float delay) {
                        System.out.println("SetCallbackDelay of callback function: " + delay);
                    }
                }
        );
        System.out.println("SetCallbackDelay returned: " + api.GetResultCodeDesc(response));

/*        response = BossaAPI.SetCallbackOrder(
                nolorderreport -> System.out.println("SetCallbackOrder of callback function: " + nolorderreport));*/
        response = api.SetCallbackOrder(
                new SetCallbackOrderDummy() {
                    @Override
                    public void invoke(NolOrderReport nolorderreport) {
                        System.out.println("SetCallbackOrder of callback function: " + nolorderreport);
                    }
                }
        );
        System.out.println("SetCallbackOrder returned: " + api.GetResultCodeDesc(response));

/*        response = BossaAPI.SetCallbackOutlook(
                outlook -> System.out.println("SetCallbackOutlook of callback function: " + outlook));*/
        response = api.SetCallbackOutlook(
                new SetCallbackOutlookDummy() {
                    @Override
                    public void invoke(String outlook) {
                        System.out.println("SetCallbackOutlook of callback function: " + outlook);
                    }
                }
        );
        System.out.println("SetCallbackOutlook returned: " + api.GetResultCodeDesc(response));

        //response = BossaAPI.Initialize();
        response = api.Initialize("BOS;BOS");
        System.out.println("Initialize returned: " + api.GetResultCodeDesc(response));

        response = api.SetCallback(
                new BossaAPIInterface.SetCallbackDummy() {
                    @Override
                    public void invoke(NolRecentInfo nolrecentinfo) {
                        System.out.println("SetCallback of callback function: " + nolrecentinfo);
                        int offerSize = nolrecentinfo.offers.bidask_table.size;
                        System.out.println(" SetCallback bid ask: " + nolrecentinfo.offers.bidask_table.toArray(offerSize)[0]);
                    }
                }
        );
        System.out.println("SetCallback returned: " + api.GetResultCodeDesc(response));
        System.out.println(api.Get_Version());
        //#TODO api działa - teraz należy przetestować każdą funkcję z osobna i napisać ściągacz danych

        NolTickers tickers = api.InitListTickers();
        api.GetTickers(tickers,TypeofList.All,null);

        System.out.println("Number of tickers: " + tickers.size);
        String isin = new String(NolTicker.class.cast(tickers.ptrtickerslist.toArray(tickers.size)[100]).Isin);
        String isin2 = new String(NolTicker.class.cast(tickers.ptrtickerslist.toArray(tickers.size)[101]).Isin);
        System.out.println("ticker: " + isin);
        //api.ClearFilter();
        String isins = isin2+ ";" + isin + ";";
        System.out.println(api.GetResultCodeDesc(api.AddToFilter(isins, true)));
        //System.out.println(api.GetResultCodeDesc(api.RemFromFilter(isin2, false)));
        //System.out.println(api.GetResultCodeDesc(api.AddToFilter(isin2, false)));
        Scanner keyboard = new Scanner(System.in);
        System.out.println("enter an integer");
        @SuppressWarnings("unused") int myint = keyboard.nextInt();
    }
}