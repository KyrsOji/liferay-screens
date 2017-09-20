/**
* Copyright (c) 2000-present Liferay, Inc. All rights reserved.
*
* This library is free software; you can redistribute it and/or modify it under
* the terms of the GNU Lesser General Public License as published by the Free
* Software Foundation; either version 2.1 of the License, or (at your option)
* any later version.
*
* This library is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
* FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
* details.
*/
import UIKit
import LiferayScreens

class BlogsViewController: CardViewController, WebScreenletDelegate {

	var selectedBlogEntry:String?

	var loaded: Bool = false
	
    @IBOutlet weak var webScreenlet: WebScreenlet!

	//MARK: Init methods

	convenience init() {
		self.init(nibName: "BlogsViewController", bundle: nil)
	}
    
    func loadWebScreenlet() {
        let webScreenletConfiguration = Builder(url: "/web/westeros-hybrid/companynews").addCss(localFile: "blogs").addJs(localFile: "blogs").load()
        webScreenlet.configuration = webScreenletConfiguration
        webScreenlet.load()
        webScreenlet.delegate = self
    }


	//MARK: CardViewController
	override func pageWillAppear() {
		if !loaded {
			loadWebScreenlet()
			loaded = true
		}
	}


	//MARK: WebScreenletDelegate
    func screenlet(_ screenlet: WebScreenlet,
                   onScriptMessageNamespace namespace: String,
                   onScriptMessage message: String) {
        selectedBlogEntry = message
        cardView?.moveRight()
	}

}
