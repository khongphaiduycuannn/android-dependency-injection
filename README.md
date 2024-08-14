# Tổng quan về Dependency Injection

## Dependency Injection là gì?

Khi class A sử dụng một chức năng nào đó của class B ta nói: A có quan hệ phụ thuộc vào class B.
Ví dụ, một chiếc xe cần có động cơ mới có thể hoạt động. Trong lập trình ta có thể nói class `Car` cần thiết phải có một instance của class `Engine` mới có thể chạy được => `Car` phụ thuộc vào `Engine`.

Vậy có những các nào để cung cấp một instance của `Engine` cho `Car`?
1. Tạo instance `Engine` trực tiếp trong class `Car`.
2. Coi `Engine` như một tham số. Chương trình có thể cung cấp các phần phụ thuộc này khi class `Car` được xây dựng hoặc truyền chúng vào các hàm cần từng phần phụ thuộc.

Cách thứ 2 chính là ***Dependency Injection***. Tách một class độc lập với các biến phụ thuộc và cung cấp các phần phụ thuộc thay vì để class đó tự lấy chúng.

## Tại sao sử dụng Dependency Injection?
Tiếp tục với 2 cách tạo instance `Engine` trong ví dụng ở phần trước. Đối với cách 1

    class  Car  {  
	    private  val engine =  Engine()  
	    fun start()  { engine.start() }  
	}

Đây không phải cách sử dụng dependency injection, có một số vấn đề gặp phải đối với đoạn code trên:

- Mỗi instance của `Car` chỉ sử dụng đúng một loại `Engine` duy nhất. Nếu `Car` muốn đổi loại động cơ sử dụng, ví dụ như `ElectricEngine` hay `GasEngine` thì ta lại phải tạo thêm 2 loại `ElectricCar` và `GasCar` tương ứng.

So sách cách trên với các thứ 2, sử dụng dependency injection, ta có thể thấy một số điểm khác biệt:

    class  Car(private  val engine:  Engine)  {  
	    fun start()  { engine.start() }  
	}

- Lớp `Car` có thể sử dụng nhiều lần với nhiều loại `Engine`. Ta có thể truyền nhiều loại triển khai của lớp `Engine` sang `Car`*. Ví dụ, ta định nghĩa lớp con của `Engine` là `ElectricEngine` hoặc `GasEngine`tùy vào `Car` muốn sử dụng loại nào, sau cùng chỉ cần truyền 1 instance của nó và `Car` là code có thể hoạt động bình thường.

Qua 2 ví dụ trên có thể thấy, việc sử dụng dependency injection mang lại các lơi ích:
- Dễ dàng thấy quan hệ giữa các đối tượng.
- Tăng các đoạn mã có thể tái sử dụng.
- Dễ dàng hơn trong việc mở rộng các ứng dụng hay tính năng.

# Dependency Injection với Hilt
Hilt là một thư viện hỗ trợ dependency injection trong Android, giảm bớt việc người dùng phải tự dependency injection một cách thủ công.

## Cài đặt
Đầu tiên, thêm `hilt-android-gradle-plugin plugin` vào file project `build.gradle`:

    plugins {  
	    ...
	    id("com.google.dagger.hilt.android") version "2.50" apply false  
	}

Sau đó thêm plugin và dependency vào file app `build.gradle`:

	plugins { 
		id("kotlin-kapt")
		id("com.google.dagger.hilt.android")  
	}

	android {  
		...  
	}

	dependencies { 
		implementation("com.google.dagger:hilt-android:2.50")
		kapt("com.google.dagger:hilt-android-compiler:2.50")  
	}  
	  
	kapt { 
		correctErrorTypes =  true  
	}

## Hilt application class

Tất cả các app sử dụng Hilt đều cần có một Application class được chú thích với `@HiltAndroidApp`.

`@HiltAndroidApp` kích hoạt việc tạo mã của Hilt, bao gồm một lớp cơ sở cho ứng dụng. Nó được gắn với vòng đời của `Application` và cung cấp các dependency tương ứng.

	@HiltAndroidApp  
	class  ExampleApplication  :  Application()  {  
		...
	}

## Chèn dependencies vào các Android class

Hilt có thể cung cấp dependency cho các lớp Android được đánh chú thích `@AndroidEntryPoint`:

	@AndroidEntryPoint  
	class  ExampleActivity  :  AppCompatActivity()  {  ...  }

Hilt hiện hỗ trợ các lớp Android sau:

-   `Application`  (sử dụng  `@HiltAndroidApp`)
-   `ViewModel`  (sử dụng  `@HiltViewModel`)
-   `Activity`
-   `Fragment`
-   `View`
-   `Service`
-   `BroadcastReceiver`

Nếu chú thích một lớp Android bằng `@AndroidEntryPoint`, thì ta cũng phải chú giải các lớp Android phụ thuộc vào lớp đó. Chẳng hạn như nếu ta chú thích một Fragment, thì cũng phải chú thích mọi Activity chưa Fragment đó.

Để lấy ra các dependency được tạo, ta sử dụng chú thích `@Inject`

	@AndroidEntryPoint  
	class  ExampleActivity  :  AppCompatActivity()  { 
	
		@Inject lateinit var analytics:  AnalyticsAdapter  
	}

> **Lưu ý:** Các trường do Hilt chèn vào không thể ở chế độ private.

## Xác định các liên kết Hilt

Để chèn một dependency, Hilt cần biết cách cung cấp một instance của lớp tương ứng. Một *liên kết* chứa các thông tin cần thiết để Hilt tạo instance đó.

Một cách để tạo *liên kết* là sử dụng `@Inject` ở hàm khởi tạo:

    class  AnalyticsAdapter  @Inject  constructor(  
	    private  val service:  AnalyticsService  
    )  {  
	    ... 
    }

Lúc này, Hilt đã biết cách tạo một instance của lớp `AnalyticsAdapter`, ta có thể cung cấp các instance `AnalyticsAdapter` cho các lớp cần sử dụng nó.

    @AndroidEntryPoint  
    class  ExampleActivity  :  AppCompatActivity()  {
    
        @Inject lateinit var analytics:  AnalyticsAdapter  
    }

Tương tự, ta cũng cần xác định cách cung cấp instance của lớp `AnalyticsService`.

## Hilt modules
Đôi khi không thể chèn một class vào hàm khởi tạo vì một số lý do. Ví dụ như chèn một interface hay một lớp đến từ thư viện bên ngoài. Trong trường hợp này, ta có thể khai báo cách cung cấp chúng trong Hilt module.

Hilt module là một lớp được chú thích bằng `@Module`, nó thông báo cho Hilt biết cách cung cấp instance của một số đối tượng nhất định.

Trong Hilt module ngoài chú thích `@Module` còn cần chú thích thêm `@InstallIn` để biết mỗi module sẽ hoạt động trong lớp Android nào.

	@Module  
	@InstallIn(ActivityComponent::class)  
	abstract  class  AnalyticsModule  {  }

Một số thành phần có thể truyền vào `@InstallIn`:
- `SingletonComponent`
- `ViewModelComponent`
- `ActivityComponent`
- `FragmentComponent`
- `ViewComponent`
- `ServiceComponent`

Hilt sẽ tự động tạo và huỷ các instance của lớp thành phần được tạo theo vòng đời của các lớp Android tương ứng.

### Chèn interface instances với @Binds

    class  AnalyticsAdapter  @Inject  constructor(  
	    private  val service:  AnalyticsService  
    )  {  
	    ... 
    }

Như ví dụ trên, nếu `AnalyticsService` là một interface, ta không thể chèn trực tiếp nó vào hàm khởi tạo.

Chú thích `@Binds` trong class `Module` sẽ cho Hilt biết cách cung cấp instance của interface đó:

    interface  AnalyticsService  {  
	    fun analyticsMethods()  
    }  

    class  AnalyticsServiceImpl  @Inject  constructor() : AnalyticsService  {  ...  }  
      
    @Module  
    @InstallIn(ActivityComponent::class)  
    abstract  class  AnalyticsModule  {  
    
	    @Binds  
	    abstract  fun bindAnalyticsService(
		    analyticsServiceImpl:  AnalyticsServiceImpl 
	    ):  AnalyticsService  
    }

Hàm được chú thích sẽ cung cấp các thông tin sau cho Hilt:

- Kiểu dữ liệu trả về cho Hilt biết cần cung cấp instance cho interface nào.
- Tham số hàm cho Hilt biết lớp implement nào của interface được sử dụng.
> **Lưu ý:** tên hàm không quan trọng, có thể đặt tùy ý.

### Chèn instances với @Provides

Interface không phải là trường hợp duy nhất không thể chèn hàm khởi tạo. Ta cũng không thể chèn hàm khởi tạo nếu bạn không sở hữu lớp đó vì nó đến từ thư viện bên ngoài (Retrofit, Room, ...), hoặc nếu instance được tạo bằng builder pattern.

Lại xét ví dụ `AnalyticsService`, nếu không trực tiếp sở hữu lớp `AnalyticsService`, thì ta có thể cho Hilt biết cách cung cấp các instance của lớp này bằng cách tạo một hàm bên trong Hilt module và chú thích hàm đó bằng `@Provides`.

    @Module  
    @InstallIn(ActivityComponent::class)  
    object  AnalyticsModule  {  
    
	    @Provides  fun provideAnalyticsService(): AnalyticsService  {  
		    return  Retrofit.Builder()
			    .baseUrl("https://example.com")
			    .build()
			    .create(AnalyticsService::class.java)  
	    }  
    }

Hàm được chú thích sẽ cung cấp các thông tin sau cho Hilt:

- Kiểu dữ liệu trả về cho Hilt biết cần cung cấp instance cho lớp nào.
- Tham số hàm cho Hilt biết các dependency cần cung cấp.
- Phần thân hàm cho Hilt biết cách khởi tạo instance của lớp.
> **Lưu ý: ** tên hàm không quan trọng, có thể đặt tùy ý.

### Cung cấp nhiều liên kết cho cùng một lớp

Trong trường hợp tan cần Hilt cung cấp các cách triển khai khác nhau của cùng một lớp làm dependency, ta phải cung cấp cho Hilt nhiều liên kết khác nhau.

Trong trường hợp này, ta có thể định nghĩa một chú thích mới xác định liên kết nào sẽ được sử dụng.
Giả sử cần cung cấp `AnalyticsService` theo 2 cách khác nhau, t có thể làm như sau:

Khai báo:

	@Qualifier
	annotation class FirstAnalyticsService

	@Qualifier
	annotation class SecondAnalyticsService
	
	@Module  
	@InstallIn(ActivityComponent::class)  
	object  AnalyticsModule  {  
		
		@FirstAnalyticsService
		@Provides  
		fun provideAnalyticsService(): AnalyticsService  {  
			 return FirstAnalyticsService()
		}  

		@SecondAnalyticsService
		@Provides  
		fun provideAnalyticsService(): AnalyticsService  {  
			 return SecondAnalyticsService()
		}  
	}

Sử dụng:

	@AndroidEntryPoint  
	class  ExampleActivity:  AppCompatActivity()  { 
		
		@FirstAnalyticsService
		@Inject lateinit var analyticsService : AnalyticsService  
	}

### Phạm vi của các thành phần

Theo mặc định, tất cả các dependency được Hilt cung cấp đều *không có phạm vi*. Tức là mỗi khi ứng dụng yêu cầu dependency, Hilt sẽ tạo một instance mới của lớp cần thiết.

Tuy nhiên, Hilt cũng cho phép đưa các dependency vào một phạm vi cụ thể.

    @Module  
    @InstallIn(ActivityComponent::class)  
    abstract  class  AnalyticsModule  {  
	    
	    @ActivityScope
	    @Binds  
	    abstract  fun bindAnalyticsService(
		    analyticsServiceImpl:  AnalyticsServiceImpl 
	    ):  AnalyticsService  
    }

Ở ví dụ trên, ta khai báo `AnalyticsService` trong `ActivityScope`. Nghĩa là trong một activity chỉ sử dụng chung một instance của lớp AnalyticsService.

Một số phạm vi có thể sử dụng:
<table>
    <tr>
        <th><p>Lớp tương ứng</p></th>
        <th><p>InstallIn Component</p></th>
        <th><p>Chú thích</p></th>
    </tr>
    <tr>
        <td>Application</td>
        <td>SingletonComponent</td>
        <td>@Singleton</td>
    </tr>
    <tr>
        <td>ViewModel</td>
        <td>ViewModelComponent</td>
        <td>@ViewModelScoped</td>
    </tr>
    <tr>
        <td>Activity</td>
        <td>ActivityComponent</td>
        <td>@ActivityScoped</td>
    </tr>
    <tr>
        <td>Fragment</td>
        <td>FragmentComponent</td>
        <td>@FragmentScoped</td>
    </tr>
    <tr>
        <td>View</td>
        <td>ViewComponent</td>
        <td>@ViewScoped</td>
    </tr>
    <tr>
        <td>Service</td>
        <td>ServiceComponent</td>
        <td>@ServiceScoped</td>
    </tr>
</table>

> **Lưu ý: ** Chú thích phạm vi chỉ có thể sử dụng trong InstallIn Component tương ứng.

Ví dụ:    
Đúng ✔️ 👇:

		@Module  
	    @InstallIn(ActivityComponent::class)  
	    abstract  class  AnalyticsModule  {  
		    
		    @ActivityScope
		    @Binds  
		    abstract fun bindAnalyticsService...
	    }

Không đúng ❌ 👇:

		@Module  
	    @InstallIn(SingletonComponent::class)
	    abstract  class  AnalyticsModule  {  
		    
		    @ActivityScope
		    @Binds  
		    abstract fun bindAnalyticsService...
	    }

## Chèn dependencies trong class không được hỗ trợ bởi Hilt

Hilt hỗ trợ cho các lớp phổ biến nhất trên Android. Tuy nhiên, ta có thể cần chèn trường trong các lớp mà Hilt không hỗ trợ bằng cách chú thích với `@EntryPoint`.

Chẳng hạn như Hilt không trực tiếp hỗ trợ content provider. Nếu muốn content provider sử dụng Hilt để lấy ra một số dependency, ta cần tạo một interface được chú thích với `@EntryPoint` với các loại dependency tương ứng cần cung cấp.
Chú thích thêm `@InstallIn` để xác định lớp được sử dụng ở thành phần Android nào.

    class  ExampleContentProvider : ContentProvider()  {
    
	    @EntryPoint  
	    @InstallIn(SingletonComponent::class)  
	    interface  ExampleContentProviderEntryPoint  {  
		    fun analyticsService():  AnalyticsService  
	    }  
    }

Để lấy ra một instance của `ExampleContentProviderEntryPoint`, ta sử dụng  lớp `EntryPointAccessors` :

    class  ExampleContentProvider:  ContentProvider()  {
    
	    override  fun query():  Cursor  {  
		    val hiltEntryPoint =  EntryPointAccessors
			    .fromApplication(applicationContext,  ExampleContentProviderEntryPoint::class.java)  
			    
		    val analyticsService = hiltEntryPoint.analyticsService()  
	    }  
    }

## Tham khảo

- [Dependency injection with Hilt | Android
  Developers](https://developer.android.com/training/dependency-injection/hilt-android#component-default)

- [Using Hilt in your Android   
  app](https://developer.android.com/codelabs/android-hilt#0)
