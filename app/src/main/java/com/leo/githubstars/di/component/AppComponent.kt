package com.leo.githubstars.di.component;


import android.app.Application
import com.leo.githubstars.application.MyGithubStarsApp
import com.leo.githubstars.di.module.*
import com.leo.githubstars.ui.splash.SplashViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


/**
 * Component에 연결할 modles.
 * AndroidSupportInjectionModule을 사용하기 위해 AndroidInjector를 상속 받는다.
 * @author LeoPark
 */
@Singleton
@Component(modules = [
    (AppModule::class),
    (ActivityModule::class),
    (NetworkDataModule::class),
    (LocalDataModule::class),
    (RepositoryModule::class),
    (UtilModule::class),
    (AndroidSupportInjectionModule::class)])

/**
 * AppComponent Interface
 * Application의 연결을 도울 AndroidInjector를 상속받고, 제네릭으로 MyGithubApp 클래스를 정의 한다.
 **/
interface AppComponent : AndroidInjector<MyGithubStarsApp> {

    /**
     * 컴포넌트를 생성하기 위한 Builder용 애노테이션이다.
     * Abstract 클래서 또는 interface에 @Componet.Builder 애노테이션을 붙인다.
     */
    @Component.Builder
    interface Builder {

        /**
         * Builder는 반드시 Componet(AppComponet)를 반환하는 메소드와 Builder를 반환하면서 컴포넌트가 필요로 하는 모듈을
         * 파라미터로 받는 메소들 가지고 있어야한다.
         * 이 조건을 충전하지 못하면 컴파일시 에러가 발생한다.
         *
         * @BindsInstance 는 @Componet.Builder의 메소드에 추가하여 객체를 컴포넌트가 가지고 있는 특정키에 바인딩하게 된다.
         * 빌더에게 인스턴스(application(), build())를 넘겨주고 컴포넌트가 이 인스턴스들을 관리하게 되고 요청시 인스터스들을 넘겨준다.
         */
        @BindsInstance
        fun application(application: Application): AppComponent.Builder
        fun build(): AppComponent
    }

    /**
     * Member-Injection Method
     * 의존성을 주입시킬 객체를 메소드의 파라미터로 넘기는 방법이다.
     * 멤버인젝션 메소드를 호출하게되면 타켓(파라미터) 클래스 내의 @Inject 필드에 객체를 주입받게 된다.
     */
    override fun inject(instance: MyGithubStarsApp)

//    /**
//     * ViewModels
//     */
//    fun inject(mainViewModel: MainViewModel)
//    fun inject(splashViewModel: SplashViewModel)
}
