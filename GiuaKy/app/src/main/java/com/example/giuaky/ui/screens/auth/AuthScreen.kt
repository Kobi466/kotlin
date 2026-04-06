package com.example.giuaky.ui.screens.auth

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.giuaky.CrudLogic
import com.example.giuaky.ui.components.AppPrimaryButton
import com.example.giuaky.ui.components.AppSectionCard
import com.example.giuaky.ui.components.AppTextField
import com.example.giuaky.ui.state.AuthMode
import com.example.giuaky.ui.theme.AccentOrange
import com.example.giuaky.ui.theme.AppSurfaceSoft
import com.example.giuaky.ui.theme.TextPrimary
import com.example.giuaky.ui.theme.TextSecondary

@Composable
fun AuthScreen(
    authMode: AuthMode,
    email: String,
    password: String,
    confirmPassword: String,
    isBusy: Boolean,
    onAuthModeChange: (AuthMode) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val isRegisterMode = authMode == AuthMode.REGISTER

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(AccentOrange.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                tint = AccentOrange,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = CrudLogic.authTitle(authMode),
            color = TextPrimary,
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = if (isRegisterMode) {
                "Tao tai khoan moi de xem note. Tai khoan dang ky tu app se la user."
            } else {
                "Dang nhap de quan ly note. Admin co them quyen them, sua va xoa."
            },
            color = TextSecondary,
            lineHeight = 22.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AuthModeButton(
                text = "Login",
                selected = authMode == AuthMode.LOGIN,
                enabled = !isBusy,
                modifier = Modifier.weight(1f),
                onClick = { onAuthModeChange(AuthMode.LOGIN) }
            )

            AuthModeButton(
                text = "Register",
                selected = authMode == AuthMode.REGISTER,
                enabled = !isBusy,
                modifier = Modifier.weight(1f),
                onClick = { onAuthModeChange(AuthMode.REGISTER) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        AppSectionCard {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                AppTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = "Email",
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                AppTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = "Password",
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )

                if (isRegisterMode) {
                    AppTextField(
                        value = confirmPassword,
                        onValueChange = onConfirmPasswordChange,
                        label = "Confirm password",
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation()
                    )
                }

                AppPrimaryButton(
                    text = CrudLogic.authPrimaryLabel(authMode),
                    onClick = if (isRegisterMode) onRegisterClick else onLoginClick,
                    enabled = !isBusy
                )

                TextButton(
                    onClick = {
                        onAuthModeChange(
                            if (isRegisterMode) AuthMode.LOGIN else AuthMode.REGISTER
                        )
                    },
                    enabled = !isBusy,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = if (isRegisterMode) {
                            "Da co tai khoan? Dang nhap"
                        } else {
                            "Chua co tai khoan? Dang ky"
                        },
                        color = AccentOrange
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthModeButton(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) AccentOrange else AppSurfaceSoft,
            contentColor = if (selected) TextPrimary else TextSecondary
        )
    ) {
        Text(text = text)
    }
}
