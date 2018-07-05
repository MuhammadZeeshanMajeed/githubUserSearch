package com.seainfosys.interview42.network;

/**
 * @author Muhammad Zeeshan
 * Seamless Distribution Systems™
 * Author Email: zeeshan.ue@hotmail.com
 * Created on: 6/7/18
 */
public interface NetworkInteractor
{
    void onRequestStart();

    void onRequestCompleted(String response);

    void onRequestError(String s);
}
