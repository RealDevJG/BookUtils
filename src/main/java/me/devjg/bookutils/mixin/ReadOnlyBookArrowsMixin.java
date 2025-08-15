package me.devjg.bookutils.mixin;

import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.widget.PageTurnWidget;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BookScreen.class)
abstract class ReadOnlyBookArrowsMixin {
	@Shadow private int pageIndex;
	@Shadow private PageTurnWidget nextPageButton;
	@Shadow private PageTurnWidget previousPageButton;

	@Shadow abstract void updatePageButtons();
	@Shadow abstract int getPageCount();

	@Inject(method = "goToPreviousPage", at = @At("HEAD"), cancellable = true)
	private void onGoToPreviousPage(CallbackInfo ci) {
		int maxValue = getPageCount();
		pageIndex = (pageIndex - 1 + maxValue) % maxValue;

		updatePageButtons();
		ci.cancel();
	}

	@Inject(method = "goToNextPage", at = @At("HEAD"), cancellable = true)
	private void onGoToNextPage(CallbackInfo ci) {
		int maxValue = getPageCount();
		pageIndex = (pageIndex + 1) % maxValue;

		updatePageButtons();
		ci.cancel();
	}

	@Inject(method = "updatePageButtons", at = @At("HEAD"), cancellable = true)
	private void onUpdatePageButtons(CallbackInfo ci) {
		nextPageButton.visible = true;
		previousPageButton.visible = true;

		ci.cancel();
	}
}
