package com.example.demo.aop;


/**
 * ログ出力処理
 * -----------------------------------------------------------------------------
 * (変更履歴) Yoshino
 * 2020/7/7:作成。全モジュールの詳細調査中
 * 2020/7/29: バッチ処理対応 試験完了
 * -----------------------------------------------------------------------------
 * ------------- (以下、コーディングの注意点)
 * 
 * とりあえず動かすための手順
 * 事前にlogbackが何なのか軽く調査するのを推奨。
 * 
 * 1:基底クラスのBaseHandlerInterceptorを作成する(本クラスには必要ないが、LoggingFunctionNameInterceptorで使う)
 * 2:上記の2つのInterceptorクラスを作成する
 * 3:乱数発生器のXORShiftRandomを作成する(必須ではないが簡単に質のいい乱数を作成できる)
 * 4:application.ymlで以下の項目を設定をする
 * spring.profiles.active="環境名"
 * logging.level="各環境のログ出力レベル"
 * 5:logback_spring.xmlを設定する。
 * ※最新版のlogbackでは"SizeAndTimeBasedRollingPolicy"というローテーションポリシーを使っている。
 * 教材の内容は古いので注意すること。上手く動かない場合は以下のドキュメントを読んで対応する。
 * https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto-logging 
 * 
 */

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.XORShiftRandom;

import lombok.val;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RequestTrackingInterceptor extends BaseHandlerInterceptor {
	
	
	private static final ThreadLocal<Long> startTimeHolder = new ThreadLocal<>();
	
	private static final String HEADER_X_TRACK_ID = "X-Track-Id";
	
	//乱数発生器
	private final XORShiftRandom random = new XORShiftRandom();
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // コントローラーの動作前
		
		//現在時間を記録
		val beforeNanoSec = System.nanoTime();
		startTimeHolder.set(beforeNanoSec);
		
		//TrackingID
		val trackId = getTrackId(request);
		
        return true;
    }


	@Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 処理完了後
		val beforeNanoSec = startTimeHolder.get();
		if (beforeNanoSec == null) {
			return;
		}
		
		val elapsedNanoSec = System.nanoTime() - beforeNanoSec;
        val elapsedMilliSec = NANOSECONDS.toMillis(elapsedNanoSec);
		log.info("path={}, method={}, Elapsed {}ms.", request.getRequestURI(),request.getMethod(),elapsedMilliSec);
		
		//破棄する
		startTimeHolder.remove();
    }
	
	/**
	 * トラッキングＩＤを取得する
	 * 
	 * @param request
	 * @return
	 */
    private String getTrackId(HttpServletRequest request) {
		// TODO 自動生成されたメソッド・スタブ
    	String trackId = request.getHeader(HEADER_X_TRACK_ID); 
    	if(trackId ==null) {
    		int seed = Integer.MAX_VALUE;
    		trackId = String.valueOf(random.nextInt(seed));
    	}
		return trackId;
	}
	

}
