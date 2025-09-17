package com.example.appstore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ViewModel : ViewModel() {

    private val _AppList = MutableStateFlow<List<AppItem>>(emptyList())
    val AppList: StateFlow<List<AppItem>> = _AppList

    // Derived flows
    val sortedAppList = _AppList.map { list ->
        list.sortedByDescending { it.downloads }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val kidsAppList = _AppList.map { list ->
        list.filter { it.kids }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val subjects = _AppList.map { list ->
        list.mapNotNull { it.subject }.distinct()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Firebase init
    init {
        val database = FirebaseDatabase.getInstance()
        database.setPersistenceEnabled(true)
        val dref = database.getReference("top_charts")
        dref.keepSynced(true)

        dref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(AppItem::class.java) }
                _AppList.value = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}

