package net.sourceforge.simcpux;


import org.json.JSONObject;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PayActivity extends Activity {
	
	private IWXAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		
		api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");

		Button appayBtn = (Button) findViewById(R.id.appay_btn);
		appayBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Button payBtn = (Button) findViewById(R.id.appay_btn);
				payBtn.setEnabled(false);
				Toast.makeText(PayActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
		        try{
					new AsyncTask<Void,Void,byte[]>(){

						@Override
						protected byte[] doInBackground(Void... voids) {
							String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
							byte[] buf = Util.httpGet(url);
							return buf;
						}

						@Override
						protected void onPostExecute(byte[] buf) {
							super.onPostExecute(buf);
							if (buf != null && buf.length > 0) {
								String content = new String(buf);
								Log.e("get server pay params:",content);
								try {
									JSONObject json = new JSONObject(content);
									if (null != json && !json.has("retcode")) {
										final PayReq req = new PayReq();
										//req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
										req.appId = json.getString("appid");
										req.partnerId = json.getString("partnerid");
										req.prepayId = json.getString("prepayid");
										req.nonceStr = json.getString("noncestr");
										req.timeStamp = json.getString("timestamp");
										req.packageValue = json.getString("package");
										req.sign = json.getString("sign");
										req.extData = "app data"; // optional
										Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
										// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
										api.sendReq(req);
									} else {
										Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
										Toast.makeText(PayActivity.this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
									}
								}catch (Exception e){
									e.printStackTrace();
								}
							}else{
								Log.d("PAY_GET", "服务器请求错误");
								Toast.makeText(PayActivity.this, R.string.servererror, Toast.LENGTH_SHORT).show();
							}
						}
					}.execute();
		        }catch(Exception e){
		        	Log.e("PAY_GET", "异常："+e.getMessage());
		        	Toast.makeText(PayActivity.this, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
		        }
		        payBtn.setEnabled(true);
			}
		});		
		Button checkPayBtn = (Button) findViewById(R.id.check_pay_btn);
		checkPayBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
				Toast.makeText(PayActivity.this, String.valueOf(isPaySupported), Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}
