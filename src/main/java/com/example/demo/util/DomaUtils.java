package com.example.demo.util;

import org.seasar.doma.jdbc.SelectOptions;

import com.example.demo.dto.Pageable;

public class DomaUtils {

    /**
     * SearchOptionsを作成して返す
     *
     * @return
     */
	public static SelectOptions createSelectOptions() {
		return SelectOptions.get();
	}
	
    /**
     * SearchOptionsを作成して返す
     *
     *@param pagable
     * @return
     */
	
	public static SelectOptions createSelectOptions(Pageable pageable) {
		int page = pageable.getPage();
		int perpage = pageable.getPerpage();
		return createSelectOptions(page,perpage);
	}
	
    /**
     * SearchOptionsを作成して返す
     *
     *@param page
     *@param perpage
     * @return
     */
	
	public static SelectOptions createSelectOptions(int page,int perpage) {
		int offset = (page - 1) * perpage;
		return SelectOptions.get().offset(offset).limit(perpage);
	}
	
	
}
