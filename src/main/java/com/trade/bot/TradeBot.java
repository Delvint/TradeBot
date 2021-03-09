package com.trade.bot;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.domain.general.Asset;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.market.TickerPrice;
import com.trade.bot.entity.Arbitrage;
import com.trade.bot.entity.Trade;
import com.trade.bot.entity.currencies.Bitcoin;
import com.trade.bot.entity.currencies.Currency;
import com.trade.bot.entity.currencies.Ethereum;
import com.trade.bot.entity.currencies.Euro;
import com.trade.bot.service.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TradeBot {

    //Services
    private AnaylzeService anaylzeService;
    private ArbitrageService arbitrageService;
    private CurrencyService currencyService;
    private DatabaseService databaseService;
    private DockerSerivce dockerSerivce;

    //Helper
    private Database database;

    public static void main(String[] args) throws InterruptedException {

        TradeBot tradeBot = new TradeBot();
        tradeBot.load();
        tradeBot.start();

    }

    public void load(){
        System.out.println("Load Services ...");
        anaylzeService = (AnaylzeService) ServiceLocator.getService("AnaylzeService");
        arbitrageService = (ArbitrageService) ServiceLocator.getService("ArbitrageService");
        currencyService = (CurrencyService) ServiceLocator.getService("CurrencyService");
        databaseService = (DatabaseService) ServiceLocator.getService("DatabaseService");
        dockerSerivce = (DockerSerivce) ServiceLocator.getService("DockerSerivce");

        System.out.println("Load Database ...");
        database = new Database(databaseService, dockerSerivce);

    }

    public void start(){
        try{
            List<TickerPrice> allPrices = ApiService.restClient.getAllPrices();
            ExchangeInfo info = ApiService.restClient.getExchangeInfo();
            arbitrageService.doArbitrageForAll(allPrices, info.getSymbols());
        }catch (InterruptedException ex){

        }

    }

    /*
    private void statisticExample(){
        Bitcoin bitcoin = new Bitcoin();
        Euro euro = new Euro();
        try {
            Future<TickerPrice> tickerPriceFuture = CurrencyService.getInstance().getEuroPriceAsync(bitcoin);
            TickerPrice tickerPrice = tickerPriceFuture.get();
            System.out.println(tickerPrice.getPrice()+"async");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        TickerPrice tickerPrice = CurrencyService.getInstance().getEuroPrice(bitcoin);
        System.out.println((tickerPrice.getPrice()+"sync"));

        AnaylzeService.getInstance().analyzeOrderBook(bitcoin, euro);
    }*/
}
