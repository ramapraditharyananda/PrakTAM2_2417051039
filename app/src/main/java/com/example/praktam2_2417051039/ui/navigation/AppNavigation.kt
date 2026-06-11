package com.example.praktam2_2417051039.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.praktam2_2417051039.data.model.Productivity
import com.example.praktam2_2417051039.data.repository.ProductivityRepository
import com.example.praktam2_2417051039.viewmodel.UserViewModel
import com.example.praktam2_2417051039.ui.screen.profile.AboutScreen
import com.example.praktam2_2417051039.ui.screen.home.DetailScreen
import com.example.praktam2_2417051039.ui.screen.home.EditScreen
import com.example.praktam2_2417051039.ui.screen.profile.FaqScreen
import com.example.praktam2_2417051039.ui.screen.history.HistoryScreen
import com.example.praktam2_2417051039.ui.screen.home.HomeScreen
import com.example.praktam2_2417051039.ui.screen.auth.LandingScreen
import com.example.praktam2_2417051039.ui.screen.auth.LoginScreen
import com.example.praktam2_2417051039.ui.screen.profile.ProfileScreen
import com.example.praktam2_2417051039.ui.screen.auth.RegisterScreen
import com.example.praktam2_2417051039.ui.screen.home.RiskAnalysisScreen
import com.example.praktam2_2417051039.ui.screen.auth.UserAccount
import com.example.praktam2_2417051039.ui.theme.Background
import com.example.praktam2_2417051039.ui.theme.Navy
import com.example.praktam2_2417051039.ui.theme.SoftBlue
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    data object Landing : Screen("landing")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object History : Screen("history")
    data object Profile : Screen("profile")
    data object Analysis : Screen("analysis")
    data object Faq : Screen("faq")
    data object About : Screen("about")
    data object Add : Screen("edit")
    data object Detail : Screen("detail/{id}") {
        fun createRoute(id: String) = "detail/$id"
    }
    data object DetailReadOnly : Screen("detail_readonly/{id}") {
        fun createRoute(id: String) = "detail_readonly/$id"
    }
    data object Edit : Screen("edit/{id}") {
        fun createRoute(id: String) = "edit/$id"
    }
}

data class BottomItem(val label: String, val route: String, val icon: ImageVector)

@Composable
fun DeadlineRiskApp() {
    val navController = rememberNavController()
    val repository = remember { ProductivityRepository() }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var tasks by remember { mutableStateOf<List<Productivity>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var users by remember { mutableStateOf<List<UserAccount>>(emptyList()) }
    val userViewModel: UserViewModel = viewModel()

    fun refreshData() {
        scope.launch {
            isLoading = true
            // Repository selalu berhasil — fallback ke dummy jika offline
            val result = repository.getProductivity()
            isLoading = false
            result.onSuccess { tasks = it }
        }
    }

    fun toggleDone(id: String) {
        val task = tasks.firstOrNull { it.id == id } ?: return
        tasks = repository.updateProductivity(task.copy(statusSelesai = !task.statusSelesai))
    }

    val bottomItems = listOf(
        BottomItem("Beranda", Screen.Home.route, Icons.Default.Home),
        BottomItem("Riwayat", Screen.History.route, Icons.Default.List),
        BottomItem("Profil", Screen.Profile.route, Icons.Default.Person)
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = currentRoute in bottomItems.map { it.route }

    Scaffold(
        containerColor = Background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(items = bottomItems, currentRoute = currentRoute, onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
        }
    ) { padding ->
        NavHost(navController = navController, startDestination = Screen.Landing.route) {

            composable(Screen.Landing.route) {
                LandingScreen(
                    onLogin = { navController.navigate(Screen.Login.route) },
                    onRegister = { navController.navigate(Screen.Register.route) }
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    users = users,
                    onLoginSuccess = { loggedInUser ->
                        userViewModel.loadFromAccount(loggedInUser.nama, loggedInUser.email, loggedInUser.password)
                        refreshData()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Landing.route) { inclusive = true }
                        }
                    },
                    onRegister = {
                        navController.navigate(Screen.Register.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = { newUser ->
                        users = users + newUser
                        scope.launch { snackbarHostState.showSnackbar("Akun berhasil dibuat! Silakan masuk.") }
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    tasks = tasks.filter { !it.statusSelesai },
                    allTasks = tasks,
                    isLoading = isLoading,
                    padding = padding,
                    onRetry = { refreshData() },
                    onDetail = { navController.navigate(Screen.Detail.createRoute(it)) },
                    onAdd = { navController.navigate(Screen.Add.route) },
                    onAnalysis = { navController.navigate(Screen.Analysis.route) },
                    onToggle = { toggleDone(it) }
                )
            }

            composable(Screen.History.route) {
                HistoryScreen(
                    tasks = tasks,
                    padding = padding,
                    onDetail = { navController.navigate(Screen.DetailReadOnly.createRoute(it)) },
                    onToggle = { toggleDone(it) }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    tasks = tasks,
                    padding = padding,
                    userViewModel = userViewModel,
                    onFaq = { navController.navigate(Screen.Faq.route) },
                    onAbout = { navController.navigate(Screen.About.route) },
                    onLogout = {
                        tasks = emptyList()
                        navController.navigate(Screen.Landing.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Analysis.route) {
                RiskAnalysisScreen(tasks, onBack = { navController.popBackStack() })
            }

            composable(Screen.Faq.route) {
                FaqScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.About.route) {
                AboutScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.Add.route) {
                EditScreen(existing = null, onBack = { navController.popBackStack() }, onSave = { item ->
                    tasks = repository.addProductivity(item)
                    scope.launch { snackbarHostState.showSnackbar("Tugas berhasil ditambahkan") }
                    navController.popBackStack()
                })
            }

            composable(
                Screen.Detail.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { entry ->
                val id = entry.arguments?.getString("id").orEmpty()
                val task = tasks.firstOrNull { it.id == id }
                if (task != null) {
                    DetailScreen(
                        task = task,
                        onBack = { navController.popBackStack() },
                        onEdit = { navController.navigate(Screen.Edit.createRoute(id)) },
                        onDelete = {
                            tasks = repository.deleteProductivity(id)
                            scope.launch { snackbarHostState.showSnackbar("Tugas berhasil dihapus") }
                            navController.popBackStack()
                        },
                        onProgressChange = { value ->
                            tasks = repository.updateProductivity(task.copy(progress = value))
                        }
                    )
                }
            }

            composable(
                Screen.DetailReadOnly.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { entry ->
                val id = entry.arguments?.getString("id").orEmpty()
                val task = tasks.firstOrNull { it.id == id }
                if (task != null) {
                    DetailScreen(
                        task = task,
                        onBack = { navController.popBackStack() },
                        onEdit = {},
                        onDelete = {},
                        onProgressChange = {},
                        readOnly = true
                    )
                }
            }

            composable(
                Screen.Edit.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { entry ->
                val id = entry.arguments?.getString("id").orEmpty()
                val task = tasks.firstOrNull { it.id == id }
                EditScreen(existing = task, onBack = { navController.popBackStack() }, onSave = { item ->
                    tasks = repository.updateProductivity(item)
                    scope.launch { snackbarHostState.showSnackbar("Tugas berhasil diperbarui") }
                    navController.popBackStack()
                })
            }
        }
    }
}

@Composable
fun AppBottomBar(items: List<BottomItem>, currentRoute: String?, onNavigate: (String) -> Unit) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Navy,
                    selectedTextColor = Navy,
                    indicatorColor = SoftBlue
                )
            )
        }
    }
}