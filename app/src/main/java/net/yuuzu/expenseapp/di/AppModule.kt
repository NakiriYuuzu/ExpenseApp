package net.yuuzu.expenseapp.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.yuuzu.expenseapp.main_feature.data.data_source.ExpenseDatabase
import net.yuuzu.expenseapp.main_feature.data.repository.ExpenseRepositoryImpl
import net.yuuzu.expenseapp.main_feature.data.repository.StoreSettingRepositoryImpl
import net.yuuzu.expenseapp.main_feature.domain.repository.ExpenseRepository
import net.yuuzu.expenseapp.main_feature.domain.repository.StoreSettingRepository
import net.yuuzu.expenseapp.main_feature.domain.usecases.*
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideExpenseDatabase(app: Application): ExpenseDatabase {
        return Room.databaseBuilder(
            app,
            ExpenseDatabase::class.java,
            ExpenseDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideExpenseRepository(db: ExpenseDatabase): ExpenseRepository {
        return ExpenseRepositoryImpl(db.expenseDao)
    }

    @Singleton
    @Provides
    fun provideExpenseUseCase(repository: ExpenseRepository): ExpenseUseCase {
        return ExpenseUseCase(
            getExpenses = GetExpenses(repository),
            deleteExpense = DeleteExpense(repository),
            addExpense = AddExpense(repository),
            getExpense = GetExpense(repository),
            getSevenExpenses = GetExpenseByDate(repository),
        )
    }
}