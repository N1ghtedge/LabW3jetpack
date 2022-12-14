package com.nopalsoft.newsapp.repository

import com.nopalsoft.newsapp.model.News
import com.nopalsoft.newsapp.provider.NewsProvider
import javax.inject.Inject

interface NewsRepository {
    suspend fun getNews(country: String): List<News>
    fun getNew(title: String): News
}

class NewsRepositoryImp @Inject constructor(
    private val newsProvider: NewsProvider
): NewsRepository{
    private var news: List<News> = emptyList()

    override suspend fun getNews(country: String): List<News> {
        val apiResponse = newsProvider.topHeadLines(country).body()
        if (apiResponse?.status == "Помилка") {
            when (apiResponse.code) {
                "apiKeyMissing" -> throw java.lang.Exception()
                "apiKeyInvalid" -> throw java.lang.Exception()
                else -> throw Exception()
            }
        }
        news = apiResponse?.articles ?: emptyList()
        return news
    }

    override fun getNew(title: String): News =
        news.first { it.title == title }

}