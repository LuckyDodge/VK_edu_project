package com.example.tosh

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tosh.ui.theme.ToshTheme

@Composable
fun AppDetailsScreen(
    app: AppInfo,
    showcaseApps: List<AppInfo>,
    onBackClick: () -> Unit,
    onShowcaseAppClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        DetailsToolbar(onBackClick = onBackClick)
        AppDetailsHeader(
            app = app,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(stringResource(R.string.install))
        }
        AppDescription(
            description = app.description,
            modifier = Modifier.padding(16.dp),
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        Developer(
            name = app.developer,
            modifier = Modifier.padding(16.dp),
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Showcase(
            apps = showcaseApps,
            onAppClick = onShowcaseAppClick,
            modifier = Modifier.padding(vertical = 16.dp),
        )
    }
}

@Composable
private fun DetailsToolbar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBackClick) {
                Text(
                    text = "‹",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 36.sp,
                )
            }
            Text(
                text = stringResource(R.string.app_details_title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

@Composable
private fun AppDetailsHeader(
    app: AppInfo,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppIcon(
            app = app,
            modifier = Modifier.size(112.dp),
        )
        Spacer(Modifier.width(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = app.category,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = app.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = app.developer,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("${app.ageRating}+")
                Text("${app.sizeMb} MB")
            }
        }
    }
}

@Composable
private fun AppDescription(
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.app_details_description),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun Developer(
    name: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = name,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = stringResource(R.string.app_details_developer),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun Showcase(
    apps: List<AppInfo>,
    onAppClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { ShowcasePageCount })

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.showcase_title),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(12.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(370.dp),
        ) { page ->
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                apps
                    .drop(page * AppsPerPage)
                    .take(AppsPerPage)
                    .forEach { app ->
                        AppListItem(
                            app = app,
                            onClick = onAppClick,
                        )
                    }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(ShowcasePageCount) { page ->
                val color = if (pagerState.currentPage == page) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                }
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(8.dp)
                        .background(color, CircleShape),
                )
            }
        }
    }
}

private const val ShowcasePageCount = 3
private const val AppsPerPage = 3

@Preview(showBackground = true)
@Composable
private fun AppDetailsScreenPreview() {
    ToshTheme(dynamicColor = false) {
        AppDetailsScreen(
            app = sampleApps.first(),
            showcaseApps = sampleApps,
            onBackClick = {},
            onShowcaseAppClick = {},
        )
    }
}
