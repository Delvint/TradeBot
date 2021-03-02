package com.trade.bot.service;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.domain.general.Asset;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.exception.BinanceApiException;
import com.trade.bot.entity.currencies.Currency;
import com.trade.bot.entity.currencies.Euro;
import org.javatuples.Pair;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class CurrencyService extends ApiService {

    private static ResourceBundle currencyBundle = ResourceBundle.getBundle("currency");

    private static CurrencyService instance;

    public static CurrencyService getInstance() {
        if(instance == null){
            instance = new CurrencyService();
        }
        return instance;
    }

    public Future<TickerPrice> getEuroPriceAsync(Currency currency)  throws InterruptedException {
        Euro euro = new Euro();
        return getPriceAsync(currency, euro);
    }

    public TickerPrice getEuroPrice(Currency currency){
        Euro euro = new Euro();
        return getPrice(currency, euro);
    }

    public Future<TickerPrice> getPriceAsync(Currency currency1, Currency currency2){
        BinanceApiCallback<TickerPrice> callback;
        CompletableFuture<TickerPrice> tickerPriceCompletableFuture = new CompletableFuture<>();
        restAsyncClient.getPrice(currency1.getName() + currency2.getName(), (response)->{
            tickerPriceCompletableFuture.complete(response);});
        return tickerPriceCompletableFuture;
    }

    public Future<Double> getPriceAsDoubleAsync(Currency currency1, Currency currency2){
        BinanceApiCallback<Double> callback;
        CompletableFuture<Double> tickerPriceCompletableFuture = new CompletableFuture<>();
        restAsyncClient.getPrice(currency1.getName() + currency2.getName(), (response)->{
            tickerPriceCompletableFuture.complete(Double.parseDouble(response.getPrice()));});
        return tickerPriceCompletableFuture;
    }

    public TickerPrice getPrice(Currency currency1, Currency currency2){
        return restClient.getPrice(currency1.getName() + currency2.getName());
    }

    public Double getPriceAsDouble(Currency currency1, Currency currency2){
        Double price;
        try{
            price = Double.parseDouble(restClient.getPrice(currency1.getName() + currency2.getName()).getPrice());
        }catch(BinanceApiException ex){
            price = 1 / Double.parseDouble(restClient.getPrice(currency2.getName() + currency1.getName()).getPrice());
        }
        return price;
    }

    public Pair<Asset,Asset> getAssetFromSymbol(List<Asset> allAssets, String symbol){
        List<Asset> filteredAssets = allAssets.stream().filter(
                a-> symbol.contains(a.getAssetCode()))
                .collect(Collectors.toList()
        );
        if(filteredAssets.size() > 2){

        }
        return new Pair<>(filteredAssets.get(0), filteredAssets.get(1));
    }


}
