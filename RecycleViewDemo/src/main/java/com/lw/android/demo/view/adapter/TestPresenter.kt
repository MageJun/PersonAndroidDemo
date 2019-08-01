package com.lw.android.demo.view.adapter

object TestPresenter {

    fun requestInNewThread(position:Int,callback: Callback){
        Thread(Runnable {
            Thread.sleep(2000)
            if(callback!=null){
                callback.callBack(position)
            }
        }).start()
    }

    interface Callback{
        fun callBack(position:Int)
    }
}