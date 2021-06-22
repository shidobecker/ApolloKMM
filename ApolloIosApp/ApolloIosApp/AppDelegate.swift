//
//  AppDelegate.swift
//  ApolloIosApp
//
//  Created by Humberto Castro on 22/06/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        LoginIOSModuleKt.doInitIOSDependencies()
        let presenter = loginPresenterDI()
        presenter.login()
        return true
    }
    
}
