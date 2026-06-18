package com.example.data

import kotlinx.coroutines.flow.Flow

class ChatRepository(private val chatDao: ChatDao) {
    val allSessions: Flow<List<ChatSession>> = chatDao.getAllSessions()

    suspend fun getSessionById(sessionId: Int): ChatSession? {
        return chatDao.getSessionById(sessionId)
    }

    suspend fun insertSession(session: ChatSession): Long {
        return chatDao.insertSession(session)
    }

    suspend fun updateSession(session: ChatSession) {
        chatDao.updateSession(session)
    }

    suspend fun deleteSession(session: ChatSession) {
        chatDao.deleteSession(session)
    }

    fun getMessagesForSession(sessionId: Int): Flow<List<ChatMessage>> {
        return chatDao.getMessagesForSession(sessionId)
    }

    suspend fun insertMessage(message: ChatMessage): Long {
        return chatDao.insertMessage(message)
    }

    suspend fun deleteMessagesForSession(sessionId: Int) {
        chatDao.deleteMessagesForSession(sessionId)
    }

    val totalMessageCount: Flow<Int> = chatDao.getTotalMessageCount()
    val userMessageCount: Flow<Int> = chatDao.getUserMessageCount()
    val modelMessageCount: Flow<Int> = chatDao.getModelMessageCount()

    fun searchAllMessages(query: String): Flow<List<ChatMessage>> {
        return chatDao.searchAllMessages(query)
    }
}
