package com.example.layceramictiles.view


//class DataStoreManager(context: Context) {
//   //private val dataStore = context.createDataStore(name="savedProjects")
//   private val dataStore = context.createDataStore(name = "saved_projects")
//    private val gson = Gson()
//
//    suspend fun saveProject(project: SavedProject) {
//        dataStore.edit { preferences ->
//            preferences[stringPreferencesKey(project.name)] = gson.toJson(project)
//        }
//    }
//
//    suspend fun loadProject(name: String): SavedProject? {
//        val preferences = dataStore.data.first()
//        val json = preferences[stringPreferencesKey(name)]
//        return json?.let { gson.fromJson(it, SavedProject::class.java) }
//    }
//
//    suspend fun getAllSavedNames(): List<String> {
//        val preferences = dataStore.data.first()
//        return preferences.asMap().keys.mapNotNull { it.name }
//    }
//}

