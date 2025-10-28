package com.howdiedoodies.chatterby.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\nR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0013"}, d2 = {"Lcom/howdiedoodies/chatterby/viewmodel/FavoriteViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "favoriteDao", "Lcom/howdiedoodies/chatterby/data/FavoriteDao;", "favorites", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/howdiedoodies/chatterby/data/Favorite;", "getFavorites", "()Lkotlinx/coroutines/flow/StateFlow;", "addFavorite", "", "username", "", "removeFavorite", "favorite", "app_debug"})
public final class FavoriteViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.howdiedoodies.chatterby.data.FavoriteDao favoriteDao = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.howdiedoodies.chatterby.data.Favorite>> favorites = null;
    
    public FavoriteViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.howdiedoodies.chatterby.data.Favorite>> getFavorites() {
        return null;
    }
    
    public final void addFavorite(@org.jetbrains.annotations.NotNull
    java.lang.String username) {
    }
    
    public final void removeFavorite(@org.jetbrains.annotations.NotNull
    com.howdiedoodies.chatterby.data.Favorite favorite) {
    }
}