package com.example.vibechat.ui.screens.signupscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibechat.ui.commoncomposables.LoadNetworkImage
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpUI(
    onSignUpSuccess: () -> Unit,
    onNavigateToLoginScreen : () -> Unit,
) {
    val viewModel: SignUpViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()


    LaunchedEffect(Unit){
        viewModel.effect.collectLatest {
            when(it){
                SignUpSideEffect.NavigateToBack -> {

                }
                is SignUpSideEffect.NavigateToMatchScreen -> {
                    onSignUpSuccess()
                }
                is SignUpSideEffect.ShowError -> {

                }
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        // Gradient Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = 140.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "Create your profile",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Already have an account?",
                        color = Color.Blue,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable{onNavigateToLoginScreen()}
                        ,
                    )

                    OutlinedTextField(
                        value = uiState.userName,
                        onValueChange = {
                            viewModel.handleEvent(
                                SignUpEvent.OnUserNameChange(it)
                            )
                        },
                        label = { Text("Your user name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )


                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = {
                            viewModel.handleEvent(
                                SignUpEvent.OnNameChange(it)
                            )
                        },
                        label = { Text("Your name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )


                    OutlinedTextField(
                        value = uiState.password,
                        visualTransformation = PasswordVisualTransformation(),
                        onValueChange = {
                            viewModel.handleEvent(
                                SignUpEvent.OnPasswordChange(it)
                            )
                        },
                        label = { Text("Your password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    GenderSelectionStylish(
                        selectedGender = uiState.selectedGender,
                        onGenderSelected = {
                            viewModel.handleEvent(
                                SignUpEvent.OnSelectedGenderChange(it)
                            )
                        }
                    )

                    ProfilePicturesWidgetStylish(
                        allImages = uiState.filteredPictures,
                        selectedImage = uiState.selectedImage,
                        onImageSelected = {viewModel.handleEvent(SignUpEvent.OnSelectedImageChange(it))}
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(30.dp),
                        enabled = uiState.userName.isNotEmpty() && uiState.selectedImage.isNotEmpty(),
                        onClick = {
                            viewModel.handleEvent(SignUpEvent.OnSignUpClick)
                        }
                    ) {
                        Text(
                            text = "Get Started",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (uiState.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}
@Composable
fun GenderSelectionStylish(
    selectedGender: GENDER = GENDER.BOY,
    onGenderSelected: (GENDER) -> Unit = {}
) {
    Column {
        Text(
            text = "Gender",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            GENDER.entries.forEach { gender ->
                Card(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(20.dp)
                        .then(
                            if(selectedGender == gender) Modifier.border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(50)
                            ) else Modifier
                        )
                        .clip(RoundedCornerShape(50))
                        .clickable { onGenderSelected(gender) },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = gender.displayName.lowercase().replaceFirstChar { it.uppercaseChar() }
                    )
                }

            }
        }
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfilePicturesWidgetStylish(
    allImages: List<String> ,
    selectedImage: String,
    onImageSelected: (String) -> Unit
) {
    Column {
        Text(
            text = "Choose an avatar",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(12.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            allImages.forEach{ item ->
                val isSelected = selectedImage == item

                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .clip(CircleShape)
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else Color.LightGray,
                            shape = CircleShape
                        )
                        .clickable {
                            onImageSelected(item)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    LoadNetworkImage(
                        url = item,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}