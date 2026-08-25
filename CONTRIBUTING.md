# راهنمای مشارکت 🤝

با تشکر از علاقه‌تان به مشارکت در **پروکسی هاب و تستر کانفیگ**! این سند راهنمای کامل برای مشارکت‌کننده‌ها است.

---

## 🚀 شروع سریع

```bash
# 1. Fork مخزن در GitHub
# 2. کلون کنید
git clone https://github.com/YOUR_USERNAME/proxy-hub-android.git
cd proxy-hub-android

# 3. Remote upstream اضافه کنید
git remote add upstream https://github.com/koko99900/proxy-hub-android.git

# 4. Branch جدید بسازید
git checkout -b feature/your-feature-name

# 5. تغییرات انجام دهید، Commit و Push کنید
git add .
git commit -m "feat: your descriptive message"
git push origin feature/your-feature-name

# 6. Pull Request باز کنید
```

---

## 📋 قوانین Commit (Conventional Commits)

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types:
| Type | توضیح |
|------|-------|
| `feat` | ویژگی جدید |
| `fix` | رفع باگ |
| `docs` | تغییرات مستندات |
| `style` | فرمت‌بندی (white-space, semi-colon) |
| `refactor` | بازنویسی کد بدون تغییر رفتار |
| `test` | اضافه/تغییر تست‌ها |
| `chore` | بیلد، وابستگی‌ها، ابزارها |
| `perf` | بهبود عملکرد |
| `ci` | تغییرات CI/CD |

### Scopes (پیشنهادی):
`parser`, `tester`, `fetcher`, `db`, `ui`, `viewmodel`, `theme`, `di`, `build`, `ci`, `deps`

### مثال‌ها:
```
feat(parser): add support for Hysteria2 protocol

fix(tester): handle timeout in ping test correctly

docs(readme): update build instructions for Windows

refactor(db): migrate to Room 2.6 with KSP
```

---

## 🏗 معماری و استانداردهای کد

### معماری: MVVM + Repository + UseCase
```
UI (Compose) → ViewModel → UseCase → Repository → DataSource (Room/Network)
```

### اصول:
- **Single Responsibility** - هر کلاس یک وظیفه
- **Dependency Inversion** - وابستگی به abstraction (Interface)
- **Immutability** - `val` به جای `var`، `data class` برای مدل‌ها
- **Coroutines/Flow** - برای عملیات async، از `suspend` و `Flow` استفاده کنید
- **Sealed Classes** - برای state management (UiState, Result)

### نام‌گذاری:
| المان | قرارداد |
|------|---------|
| کلاس/اینترفیس | `PascalCase` |
| تابع/متغیر | `camelCase` |
| ثابت | `UPPER_SNAKE_CASE` |
| پکیج | `lowercase` |
| فایل layout | `snake_case.xml` |
| فایل Compose | `PascalCase.kt` |

---

## ✅ چک‌لیست Pull Request

### قبل از باز کردن PR:
- [ ] Tests passent (Unit + Instrumented)
- [ ] Lint بدون خطا: `./gradlew lintDebug`
- [ ] بیلد موفق: `./gradlew assembleDebug`
- [ ] Commit messages از Convension Commit پیروی می‌کنند
- [ ] Branch بروز است با `upstream/master` (`git pull upstream master`)
- [ ] تغییرات متمرکز و atomic هستند (یک ویژگی/باگ در هر PR)

### در توضیح PR:
- **What:** چه تغییراتی انجام شده؟
- **Why:** چرا این تغییر لازم است؟ (Issue number اگر وجود دارد)
- **How:** به طور خلاصه نحوه پیاده‌سازی
- **Screenshots:** برای تغییرات UI حتماً اسکرین‌شات بگذارید
- **Breaking Changes:** اگر API/DB schema تغییر کرده،.Migration path توضیح دهید

---

## 🧪 تست‌نویسی

### Unit Tests (JVM)
```kotlin
// app/src/test/java/com/example/data/parser/ConfigParserTest.kt
class ConfigParserTest {
    @Test
    fun `parse VLESS URI with all params`() {
        val uri = "vless://uuid@host:443?security=tls&type=ws#name"
        val result = ConfigParser.parse(uri)
        
        assertTrue(result.isSuccess)
        assertEquals("uuid", result.getOrNull()?.id)
    }
}
```

### Instrumented Tests (Android)
```kotlin
// app/src/androidTest/java/com/example/data/db/ProxyDaoTest.kt
@RunWith(AndroidJUnit4::class)
class ProxyDaoTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @Test
    fun insertAndGetProxy() = runTest {
        // test implementation
    }
}
```

### UI Tests (Roborazzi - Screenshot)
```kotlin
// app/src/test/java/com/example/ui/screens/ProxiesScreenTest.kt
class ProxiesScreenTest {
    @Test
    fun proxiesScreen_rendersCorrectly() {
        composeTestRule.setContent {
            ProxiesScreen(onProxyClick = {})
        }
        composeTestRule
            .captureRoboImage("ProxiesScreen_default")
    }
}
```

### اجرای تست‌ها:
```bash
# Unit tests
./gradlew testDebugUnitTest

# Instrumented (نیاز به امولاتور)
./gradlew connectedDebugAndroidTest

# Roborazzi record
./gradlew recordRoborazziDebug

# Verify screenshots
./gradlew verifyRoborazziDebug
```

---

## 🎨 UI/Compose Guidelines

### Theme & Styling:
- از `MaterialTheme` و `ColorScheme` استفاده کنید
- Hardcoded color نداشته باشید، از `MaterialTheme.colorScheme.primary` و...
- Typography از `MaterialTheme.typography`
- Shapes از `MaterialTheme.shapes`

### Compose Best Practices:
- `@Preview` برای همه Composableهای قابل پیش‌نمایش
- `remember` و `derivedStateOf` برای state محاسبه‌شده
- `LaunchedEffect` / `DisposableEffect` برای side-effects
- `@Stable` / `@Immutable` برای مدل‌ها
- `Modifier` به ترتیب: `fillMaxSize()`, `padding()`, `clickable()`, `background()`

### Accessibility:
- `contentDescription` برای Image/Icon
- `semantics` برای عناصر تعاملی
- Minimum touch target 48dp
- Color contrast ratio ≥ 4.5:1

---

## 🔧 تنظیمات محیط توسعه

### Android Studio:
- **Version:** Ladybug 2024.2.1+
- **Plugins:** Kotlin, Compose, KSP, Robolectric
- **Settings:** 
  - Editor → Code Style → Kotlin → `kotlin-android` style
  - Editor → Inspections → Kotlin → فعال کنید

### Gradle Properties (`gradle.properties`):
```properties
org.gradle.jvmargs=-Xmx4g -XX:+HeapDumpOnOutOfMemoryError
org.gradle.parallel=true
org.gradle.configureondemand=true
android.enableBuildCache=true
kotlin.code.style=official
```

### Pre-commit Hook (اختیاری):
```bash
# نصب pre-commit
pip install pre-commit
pre-commit install
```

`.pre-commit-config.yaml`:
```yaml
repos:
  - repo: https://github.com/pre-commit/pre-commit-hooks
    rev: v4.6.0
    hooks:
      - id: trailing-whitespace
      - id: end-of-file-fixer
      - id: check-yaml
      - id: check-added-large-files
  - repo: local
    hooks:
      - id: ktlint
        name: ktlint
        entry: ./gradlew ktlintCheck
        language: system
        types: [kotlin]
      - id: detekt
        name: detekt
        entry: ./gradlew detekt
        language: system
        types: [kotlin]
```

---

## 📦 مدیریت وابستگی‌ها (Version Catalog)

همه نسخه‌ها در `gradle/libs.versions.toml` مدیریت می‌شوند:

```toml
[versions]
kotlin = "2.0.0"
compose-bom = "2024.08.00"
room = "2.6.1"

[libraries]
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
```

### اضافه کردن وابستگی جدید:
1. در `gradle/libs.versions.toml` نسخه و کتابخانه اضافه کنید
2. در `app/build.gradle.kts`: `implementation(libs.new-library)`
3. Sync و بیلد کنید

---

## 🔐 امنیت

- **هرگز** API Key، Keystore، Password در کد commit نکنید
- از `.env` و GitHub Secrets استفاده کنید
- `secrets` Gradle plugin برای `.env` پیکربندی شده
- ProGuard/R8 برای release فعال است

---

## 📚 منابع مفید

- [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- [Jetpack Compose Guidelines](https://developer.android.com/jetpack/compose/guidelines)
- [Android Architecture Guide](https://developer.android.com/topic/architecture)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [GitHub Flow](https://guides.github.com/introduction/flow/)

---

## ❓ سوال دارید؟

- [Discussions](https://github.com/koko99900/proxy-hub-android/discussions) برای سوالات عمومی
- [Issues](https://github.com/koko99900/proxy-hub-android/issues) برای باگ/فیچر
- در PR نظر بگذارید

---

**ممنون از مشارکت شما! 🙏**
