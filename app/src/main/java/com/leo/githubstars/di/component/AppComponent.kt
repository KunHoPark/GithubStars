package com.leo.githubstars.di.component;


import android.app.Application
import com.leo.githubstars.application.MyGithubApp
import com.leo.githubstars.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton                      //Scope
@Component(modules = [          //연결할 Module을 정의 한다.
    (AppModule::class),
    (ActivityModule::class),
    (NetworkDataModule::class),
    (LocalDataModule::class),
    (RepositoryModule::class),
    (AndroidSupportInjectionModule::class)])

/**
 * AppComponent Interface
 * @author KunHOPark
 * @since 2018. 7. 29. AM 10:03
 **/
interface AppComponent : AndroidInjector<MyGithubApp> {       //Application과의 연결을 도울 AndroidInjector를 상속받고, 제네릭으로 MyGithubApp 클래스를 정의 한다.

    @Component.Builder
    interface Builder {

        //Application과의 연결을 도울 Builder를 정의 한다.
        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }

    override fun inject(instance: MyGithubApp)
}
