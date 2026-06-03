package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppDatabase
import com.example.data.ConfigRepository
import com.example.ui.screens.ColorBg
import com.example.ui.screens.ColorBorderLight
import com.example.ui.screens.ColorTextLabel
import com.example.ui.screens.ColorTextMain
import com.example.ui.screens.HubScreen
import com.example.ui.screens.LuzScreen
import com.example.ui.screens.MasScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.NavigationTab

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(this)
        val repository = ConfigRepository(database.configDao())

        setContent {
            MyApplicationTheme {
                val viewModel: AppViewModel by viewModels {
                    AppViewModel.Factory(repository)
                }

                val currentTab by viewModel.currentTab.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        CustomBottomNavigation(
                            currentTab = currentTab,
                            onTabSelected = { viewModel.selectTab(it) }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(ColorBg)
                    ) {
                        // Custom Header mimicking Material 3 Mapped HTML header bar precisely
                        HeaderBar(
                            currentTab = currentTab,
                            onSettingsClick = { viewModel.selectTab(NavigationTab.MAS) }
                        )

                        // Elegant Transition Content
                        Crossfade(
                            targetState = currentTab,
                            modifier = Modifier.weight(1f),
                            label = "screen_transitions"
                        ) { tab ->
                            when (tab) {
                                NavigationTab.HUB -> HubScreen(viewModel = viewModel)
                                NavigationTab.LUZ -> LuzScreen(viewModel = viewModel)
                                NavigationTab.MAS -> MasScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderBar(
    currentTab: NavigationTab,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val subtitleTextId = when (currentTab) {
        NavigationTab.HUB -> R.string.sub_hub
        NavigationTab.LUZ -> R.string.title_calculadora_luz
        NavigationTab.MAS -> R.string.nav_mas
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color.White)
            .border(1.dp, ColorBorderLight)
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon layout container
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF4A5C92)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚡",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Column {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTextMain,
                    lineHeight = 18.sp
                )
                Text(
                    text = stringResource(id = subtitleTextId),
                    fontSize = 11.sp,
                    color = ColorTextLabel,
                    lineHeight = 14.sp
                )
            }
        }

        // Settings / Config action gear button
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .testTag("action_settings_button")
                .clickable(onClick = onSettingsClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⚙️",
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun CustomBottomNavigation(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(ColorBg)
            .border(1.dp, ColorBorderLight)
            .navigationBarsPadding() // Safe gesture inset padding!
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            icon = "🏠",
            label = stringResource(id = R.string.nav_hub),
            isActive = currentTab == NavigationTab.HUB,
            onClick = { onTabSelected(NavigationTab.HUB) },
            modifier = Modifier.testTag("bottom_nav_hub")
        )
        BottomNavItem(
            icon = "💡",
            label = stringResource(id = R.string.nav_luz),
            isActive = currentTab == NavigationTab.LUZ,
            onClick = { onTabSelected(NavigationTab.LUZ) },
            modifier = Modifier.testTag("bottom_nav_luz")
        )
        BottomNavItem(
            icon = "➕",
            label = stringResource(id = R.string.nav_mas),
            isActive = currentTab == NavigationTab.MAS,
            onClick = { onTabSelected(NavigationTab.MAS) },
            modifier = Modifier.testTag("bottom_nav_mas")
        )
    }
}

@Composable
fun BottomNavItem(
    icon: String,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(if (isActive) Color(0xFFDDE1FF) else Color.Transparent)
                .padding(horizontal = 20.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                fontSize = 18.sp
            )
        }
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) Color(0xFF001452) else ColorTextLabel
        )
    }
}
