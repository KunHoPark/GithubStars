package com.leo.githubstars.di.component;


import android.app.Application
import com.leo.githubstars.application.MyGithubStarsApp
import com.leo.githubstars.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

// Component에 연결할 modles.
// AndroidSupportInjectionModule을 사용하기 위해 AndroidInjector를 상속 받는다.
@Singleton
@Component(modules = [
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
interface AppComponent : AndroidInjector<MyGithubStarsApp> {       //Application과의 연결을 도울 AndroidInjector를 상속받고, 제네릭으로 MyGithubApp 클래스를 정의 한다.

    // AppComponent를 생성 할 때 사용할 빌더 클래스를 정의
    @Component.Builder
    interface Builder {

        // Component에 추가로 전달할 객체. Builder로 리턴 해야 한다.
        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }

    override fun inject(instance: MyGithubStarsApp)
}
