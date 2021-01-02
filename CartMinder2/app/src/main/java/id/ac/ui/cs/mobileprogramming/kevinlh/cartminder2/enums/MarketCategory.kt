package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.enums

import android.content.Context
import androidx.core.content.ContextCompat
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R

enum class MarketCategory {
    UNSPECIFIED,
    SUPERMARKET,
    MINIMARKET,
    TRADITIONAL;

    companion object {
        fun toColor(mc: MarketCategory, ctx: Context) = when (mc) {
            UNSPECIFIED -> ContextCompat.getColor(ctx, R.color.red)
            SUPERMARKET -> ContextCompat.getColor(ctx, R.color.blue)
            MINIMARKET -> ContextCompat.getColor(ctx, R.color.green)
            TRADITIONAL -> ContextCompat.getColor(ctx, R.color.orange)
        }

        fun toImgResource(mc: MarketCategory): Int = when (mc) {
            UNSPECIFIED -> R.drawable.ic_stop
            TRADITIONAL -> R.drawable.ic_flea_market
            MINIMARKET -> R.drawable.ic_store
            SUPERMARKET -> R.drawable.ic_big_store
        }
    }
}