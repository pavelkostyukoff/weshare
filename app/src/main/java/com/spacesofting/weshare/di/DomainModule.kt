package com.spacesofting.weshare.di

import com.spacesofting.weshare.domain.usecases.AutorizeUserUseCase
import com.spacesofting.weshare.domain.usecases.CreateNewAdvertUseCase
import org.koin.dsl.module

var domainModule = module {
    //todo создаю модули
    factory <AutorizeUserUseCase>{
        AutorizeUserUseCase() //get get get
    }

    factory <CreateNewAdvertUseCase>{
        CreateNewAdvertUseCase()
    }
}