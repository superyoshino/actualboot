package com.example.demo.controller;

import static com.example.demo.WebConst.MAV_ERRORS;
import static com.example.demo.WebConst.GLOBAL_MESSAGE;
import static com.example.demo.WebConst.MESSAGE_DELETED;
import static com.example.demo.util.TypeUtils.toListType;
import static java.util.Optional.ofNullable;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Demo;
import com.example.demo.dao.UploadFileDao;
import com.example.demo.dto.Pageable;
import com.example.demo.dto.UploadFile;
import com.example.demo.model.SampleCsvModel;
import com.example.demo.model.SampleExcelModel;
import com.example.demo.model.SampleProductModel;
import com.example.demo.users.SearchUserForm;
import com.example.demo.users.User;
import com.example.demo.users.UserCriteria;
import com.example.demo.users.UserCsv;
import com.example.demo.users.UserExcel;
import com.example.demo.users.UserForm;
import com.example.demo.users.UserService;
import com.example.demo.util.MultipartFileUtils;
import com.example.demo.validator.UserFormValidator;
import com.example.demo.view.CsvView;
import com.example.demo.view.ExcelView;
import com.example.demo.view.PdfView;
//import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * ユーザー管理および各種ファイル操作クラス
 * 本クラスを当アプリの拠点とする。
 *  -----------------------------------------------------------------------------
 * (変更履歴) Yoshino
 * 2020/7/13 セキュリティ以外の処理を実装して動作確認、改修開始。DB登録がうまくいかない。
 * 2020/7/22 結合テスト中。楽観的排他制御機能要調査。
 * 2020/7/29 結合テスト完了
 * -----------------------------------------------------------------------------
 *  ------------- (以下、コーディングの注意点)
 * 
 * ※教材が各機能をバラバラに紹介していてとっかかりが分かりづらい。(というかサンプルソースを見ないと何してるか絶対わからない)
 * 動作確認が容易なログ出力、各ファイル出力機能の作成から行うのが推奨
 * ユーザ管理機能、DB、UI、 ファイルアップロード、メール機能、権限操作はやりやすい順番で自由に
 * セキュリティ機能は表示確認に邪魔なので最後に回す
 * 
 * ※Autowiredによる依存性の注入に失敗する場合がある。
 * gitのコメント要参照
 */
@Controller
@RequestMapping("/users/users")
//型が一致するformオブジェクトをセッションに格納する
@SessionAttributes(types = {SearchUserForm.class,UserForm.class })
@Slf4j
public class UserHtmlController extends AbstractHtmlController {
	
	@Autowired
	UserFormValidator userFormValidator;
	
    @Autowired
    UserService userService;
    
    @Autowired
    UploadFileDao uploadFileDao;
	
	@Autowired
	private ModelMapper modelMapper;
	 
//	@Autowired
//	PasswordEncoder passwordEncoder;
    
    @ModelAttribute("userForm")
    public UserForm userForm() {
        return new UserForm();
    }
	
    @ModelAttribute("searchUserForm")
    public SearchUserForm searchUserForm() {
        return new SearchUserForm();
    }
	
	@InitBinder("userForm")
	public void validatorBinder(WebDataBinder binder) {
		binder.addValidators(userFormValidator);
	}
	
    @Override
    public String getFunctionName() {
        return "A_USER";
    }
    
	
    /**
     * 登録画面 初期表示
     *
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String newUser(@ModelAttribute("userForm") UserForm form, Model model) {
    	log.info("newUser.form.isNew()" + form.isNew());
    	log.info("newUser.form.getId()" + form.getId());
    	log.info("newUser.form.getUploadFile()" + form.getUploadFile());
        if (!form.isNew()) {
            // SessionAttributeに残っている場合は再生成する
        	log.info("newUser.SessionAttributeに残っている場合は再生成する");
            model.addAttribute("userForm", new UserForm());
        }
        

        return "modules/users/users/new";
    }
	
	/**
	 * ユーザーフォーム 登録処理
	 * -----------------------------------------------------------------------------
	 * (変更履歴) Yoshino
	 * 2020/7/3:バリデーション作成,相関チェック作成,残課題→マッピング作成
	 * 2020/7/6:マッピング仮置き→残課題登録ロジック
	 * 2020/7/13 登録ロジック実装
	 * 2020/7/17 登録処理確認
	 * 2020/7/21 結合テスト開始
	 * 2020/7/22 結合テスト完了
	 * -----------------------------------------------------------------------------
	 * ------------- (以下、コーディングの注意点)
	 * 
	 * 現状特になし
	 * 
	 * @param form
	 * @param br
	 * @param attributes
	 * @return
	 */
	@PostMapping("/new")
    public String newUser(@Validated @ModelAttribute("userForm") UserForm form, BindingResult br,
            RedirectAttributes attributes) {
		
		log.info("登録モジュールに入ってます");

		// 入力チェックエラーがある場合は、元の画面にもどる
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            log.info("エラーがあります" + br);
            return "redirect:/users/users/new";
        }
		
		log.info("formimage"+form.getUploadFile());
		log.info("formimagesize"+form.getUploadFile().getSize());
		
		// ファイルが空の場合、nullにする
		if(form.getUploadFile().getSize() == 0) {
			log.info("newUserファイルからっぽ");
			form.setUploadFile(null);
		}
		
		//入力値からドメインオブジェクトを作成する
		val inputUser = modelMapper.map(form,User.class);
		val password = form.getPassword();
	
		log.info("formzip"+ form.getZip());
		log.info("inputuser"+inputUser.getUploadFile());
		log.info("password"+password);
		
        val image = form.getUploadFile();
        if (image != null && !image.isEmpty()) {
        	log.info("newUserimage空以外");
            val uploadFile = new UploadFile();
            MultipartFileUtils.convert(image, uploadFile);
            inputUser.setUploadFile(uploadFile);
        }
		
		
		//パスワードをハッシュ化する
		//inputUser.setPassword(passwordEncoder.encode(password));
		
		//登録する
		val createdUser = userService.create(inputUser);
		
		return "redirect:/users/users/show/" + createdUser.getId();

	}
	
    /**
     * 一覧画面 初期表示
     *
     * @param model
     * @return
     */
    @GetMapping("/find")
    public String findUser(@ModelAttribute SearchUserForm form,Model model) {
    	//入力値を詰め替える
    	val criteria = modelMapper.map(form,UserCriteria.class);
    	
        // 10件区切りで取得する
        val pages = userService.findAll(criteria, form);

        // 画面に検索結果を渡す
        model.addAttribute("pages", pages);

        return "modules/users/users/find";
    }
    
    /**
     * 検索結果
     *
     * @param form
     * @param br
     * @param attributes
     * @return
     */
    @PostMapping("/find")
    public String findUser(@Validated @ModelAttribute("searchUserForm") SearchUserForm form, BindingResult br,
    		RedirectAttributes attributes) {
    	
        // 入力チェックエラーがある場合は、元の画面にもどる
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/users/users/find";
        }
    	
    	return "redirect:/users/users/find";
    }
    
    /**
     * 詳細画面
     *
     * @param userId
     * @param model
     * @return
     */
    @GetMapping("/show/{userId}")
    public String showUser(@PathVariable Long userId, Model model) {
        // 1件取得する
    	log.info("詳細画面モジュールに入ってます" + userId +"のIDの人を探してます");
        val user = userService.findById(userId);
        model.addAttribute("user", user);
        
        log.info("詳細画面モジュールに入ってます" + user.getUploadFileId());
        
        if (user.getUploadFileId() != null) {
            // 添付ファイルを取得する
        	val uploadFileList = ofNullable(user.getUploadFileId()).map(uploadFileDao::selectById);
        	uploadFileList.ifPresent(user::setUploadFile);
        	val uploadFile = user.getUploadFile();
            log.info("詳細画面モジュールに入ってます。uploadFile" +user.getUploadFile());

            // Base64デコードして解凍する
            val base64data = uploadFile.getContent().toBase64();
            val sb = new StringBuilder().append("data:image/png;base64,").append(base64data);

            model.addAttribute("image", sb.toString());
        }
    	
    	return "modules/users/users/show";
    	
    }
    
    
    /**
     * 編集画面 初期表示
     *
     * @param userId
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/edit/{userId}")
    public String editUser(@PathVariable Long userId,@ModelAttribute ("userForm") UserForm form, Model model) {
    	// セッションから取得できる場合は、読み込み直さない
    	if(!hasErrors(model)) {
    		// 1件取得する
    		val user = userService.findById(userId);
    		
    		 // 取得したDtoをFormに詰め替える
    		modelMapper.map(user,form);
    		
    	}
    	
    	 return "modules/users/users/new";
    	
    }
    
    
    
	
    /**
     * 編集画面 更新処理
     *
     * @param form
     * @param br
     * @param userId
     * @param sessionStatus
     * @param attributes
     * @return
     */
    @PostMapping("/edit/{userId}")
    public String editUser(@Validated @ModelAttribute("userForm") UserForm form, BindingResult br,
    		 @PathVariable Long userId, SessionStatus sessionStatus, RedirectAttributes attributes) {
    	
    	// 入力チェックエラーがある場合は、元の画面にもどる
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/users/users/edit/" + userId;
        }
        
     // ファイルが空の場合、nullにする
		if(form.getUploadFile().getSize() == 0) {
			log.info("newUserファイルからっぽ");
			form.setUploadFile(null);
		}
        
        // 更新対象を取得する
        val user = userService.findById(userId);

        // 入力値を詰め替える
        modelMapper.map(form, user);

        // パスワードをハッシュ化する
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        val image = form.getUploadFile();
        if (image != null && !image.isEmpty()) {
            val uploadFile = new UploadFile();
            MultipartFileUtils.convert(image, uploadFile);
            user.setUploadFile(uploadFile);
        }
        
        // 更新する
        val updatedUser = userService.update(user);
        
        // セッションのuserFormをクリアする
        sessionStatus.setComplete();
    	
    	
    	return "redirect:/users/users/show/" + updatedUser.getId();
    }
    
    /**
     * 削除処理
     *
     * @param userId
     * @param attributes
     * @return
     */
    @PostMapping("/remove/{userId}")
    public String removeUser(@PathVariable Long userId, RedirectAttributes attributes) {
    	// 論理削除する
    	userService.delete(userId);
    	
    	// 削除成功メッセージ
    	attributes.addFlashAttribute(GLOBAL_MESSAGE,getMessage(MESSAGE_DELETED));
    	
    	return "redirect:/users/users/find";
    }
    
	/**
	 * csvダウンロード
	 * -----------------------------------------------------------------------------
	 *  (変更履歴) Yoshino
	 * 2020/7/1:出力成功。最低限動く形。残課題→Controllerとcsvview、Daoの分離、文字化け対策。
	 * 2020/7/2:controllerとviewの分離成功。文字化け対策現状不要。残課題→Daoの分離
	 * 2020/7/22:Dao実装。結合テスト完了。なぜか知らないが空のファイルも勝手に出力される。。。
	 * -----------------------------------------------------------------------------
	 * ------------- (以下、コーディングの注意点)
	 * 
	 * ※Jacksonについて軽く調査推奨。Streamの機能については理解必須。
	 * 
	 * ※CsvView.javaの教材、誤りあり。コメントのYoshino fixedを参照。
	 * 
	 * @param filename
	 * 
	 * @return
	 * 
	 */
	@GetMapping(path = "/download/{filename:.+\\.csv}")
	public ModelAndView downloadCsv(@PathVariable String filename) {
		// 全件取得する
		val users = userService.findAll(new UserCriteria(), Pageable.NO_LIMIT);
		log.info("csvdata"+users);
		
		List<UserCsv> csvList = modelMapper.map(users.getData(), toListType(UserCsv.class));

		/*単体テスト用データここから
		List<SampleCsvModel> csvList = new ArrayList<>();

		//単体テスト用データ
		SampleCsvModel data1 = new SampleCsvModel(1, "yoshino");
		SampleCsvModel data2 = new SampleCsvModel(2, "高津");

		csvList.add(data1);
		csvList.add(data2);
		単体テスト用データここまで*/

		// レスポンスを設定する。
		val view = new CsvView(UserCsv.class, csvList, filename);

		return new ModelAndView(view);
	}
	
	/**
	 * excelダウンロード
	 * -----------------------------------------------------------------------------
	 *  (変更履歴) Yoshino
	 * 2020/7/2:出力成功。文字化け対策現状不要。残課題→Daoの分離。
	 * 2020/7/22:Dao実装。結合テスト完了
	 * -----------------------------------------------------------------------------
	 * ------------- (以下、コーディングの注意点)
	 * 
	 * ※apachepoiについて軽く調査推奨。Streamの機能については理解必須。
	 * 
	 * ※ExcelView.java,UserExcel.javaで微修正あり。コメントのYoshino fixedを参照。
	 * 
	 * @param filename
	 * 
	 * @return
	 * 
	 */
	@GetMapping(path = "/download/{filename:.+\\.xlsx}")
	public ModelAndView downloadExcel(@PathVariable String filename) {
		// 全件取得する
		log.info("エクセルデータ");
		val users = userService.findAll(new UserCriteria(), Pageable.NO_LIMIT);

		/*単体テスト用データここから
		List<SampleExcelModel> excelList = new ArrayList<>();

		SampleExcelModel data1 = new SampleExcelModel("yoshino", "hiroaki","fujinetworks");
		SampleExcelModel data2 = new SampleExcelModel("ビル","ゲイツ","マイクロソフト");

		excelList.add(data1);
		excelList.add(data2);

		
		val view = new ExcelView(new UserExcel(), excelList, filename);
		単体テスト用データここまで*/
		
		// excelブック生成コールバック、データ、ダウンロード時のファイル名を指定する。
		val view = new ExcelView(new UserExcel(), users.getData(), filename);

		return new ModelAndView(view);
	}
    

	/**
	 * pdfダウンロード
	 * -----------------------------------------------------------------------------
	 * (変更履歴) Yoshino
	 * 2020/7/1:出力成功。controllerとviewの分離まで完了。残課題→帳票ControllerとDaoの分離、日本語対応、文字化け対策。
	 * 2020/7/3:日本語出力対応完了。残課題→Daoの分離
	 * 2020/7/6:文字化け対策処理、jrxmlデグレ対応。残課題→Daoの分離
	 * 2020/7/22:Dao実装。結合テスト完了
	 * 
	 * -----------------------------------------------------------------------------
	 * ------------- (以下、コーディングの注意点)
	 * 
	 * ※JasperReportsについて調査必須。
	 * 
	 * ※帳票フォーマット作成はJaspersoft-studioで行う。
	 * マーケットプレイスからインポートできない場合は、以下の公式サイトにユーザ登録してWindows版をダウンロードする。
	 * https://sourceforge.net/projects/jasperstudio/
	 * 
	 * ※"Unpatched iText found, cannot use glyph rendering"というWarningが出る場合があるが
	 * おそらく出力には影響しないので、エラーの原因調査の際は無視して良い。
	 * 
	 * ※java.lang.NoClassDefFoundError:com/lowagie/text/DocumentExceptionというエラーが出る場合はbuild.gradleに以下を記述する 
	 * compile group:'com.lowagie', name: 'itext', version: '2.1.7'
	 * 
	 * @param filename
	 * 
	 * @return
	 * 
	 */
	@GetMapping(path = "/download/{filename:.+\\.pdf}")
	public ModelAndView downloadPdf(@PathVariable String filename) {
		// 全件取得する
		val users = userService.findAll(new UserCriteria(), Pageable.NO_LIMIT);

		/*単体テスト用データここから
		List<SampleProductModel> result = new ArrayList<>();

		
		SampleProductModel data1 = new SampleProductModel("吉野", "弘晃","yoshino@fujinetworks.co.jp");
		SampleProductModel data2 = new SampleProductModel("藤井", "太郎","fujii@fujinetworks.co.jp");

		result.add(data1);
		result.add(data2);

		val view = new PdfView("/reports/users.jrxml", result, filename);
		 
		 単体テスト用データここまで*/
		//帳票レイアウト、データ、ダウンロード時のファイル名を指定する。
		val view = new PdfView("reports/users.jrxml",users.getData(),filename);

		return new ModelAndView(view);
	}
}