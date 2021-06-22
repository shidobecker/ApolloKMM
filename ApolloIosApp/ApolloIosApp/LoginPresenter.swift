//
//  LoginPresenter.swift
//  ApolloIosApp
//
//  Created by Humberto Castro on 22/06/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import shared

final class LoginPresenter {
    
    // =========================================================
    // MARK: - Private properties
    // =========================================================
    
    private let useCases: LoginUseCase
    
    // =========================================================
    // MARK: - Init
    // =========================================================
    
    init(useCases: LoginUseCase) {
        self.useCases = useCases
    }
    
    func login() {
        useCases.fetchLogin(username: "04646574667", password: "1234") { flow, error in
            flow?.collectCommon(coroutineScope: nil, callback: { dataState in
                print("VALOR MARALHOUSER: \(dataState)")
            })
            
            print(flow)
            print(error)

        }
    }
}


func loginPresenterDI() -> LoginPresenter {
    let sharedComponent = LoginIOSComponent()
    return LoginPresenter(useCases: sharedComponent.provideLoginUseCase())
}
