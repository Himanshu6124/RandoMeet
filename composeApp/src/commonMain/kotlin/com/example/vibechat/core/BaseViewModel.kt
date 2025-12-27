package com.example.vibechat.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<UIState,Event,Effect> : ViewModel() {

    abstract val initialState : UIState

    val TAG : String = this::class.simpleName.toString()

    protected val _uiState : MutableStateFlow<UIState> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    protected val _effect : MutableSharedFlow<Effect> = MutableSharedFlow()
    val effect = _effect.asSharedFlow()

    abstract fun handleEvent(event: Event)


}