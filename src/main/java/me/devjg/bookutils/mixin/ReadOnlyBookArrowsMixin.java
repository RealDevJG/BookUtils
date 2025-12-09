package me.devjg.bookutils.mixin;

import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BookViewScreen.class)
abstract class ReadOnlyBookArrowsMixin {
	@Shadow private int currentPage;

	@Shadow private PageButton forwardButton;
	@Shadow private PageButton backButton;

	@Shadow protected abstract void updateButtonVisibility();
	@Shadow protected abstract int getNumPages();

	@Inject(method = "pageBack", at = @At("HEAD"), cancellable = true)
	private void onGoToPreviousPage(CallbackInfo ci) {
		int maxValue = getNumPages();
		currentPage = (currentPage - 1 + maxValue) % maxValue;

		updateButtonVisibility();
		ci.cancel();
	}

	@Inject(method = "pageForward", at = @At("HEAD"), cancellable = true)
	private void onGoToNextPage(CallbackInfo ci) {
		int maxValue = getNumPages();
		currentPage = (currentPage + 1) % maxValue;

		updateButtonVisibility();
		ci.cancel();
	}

	@Inject(method = "updateButtonVisibility", at = @At("HEAD"), cancellable = true)
	private void onUpdatePageButtons(CallbackInfo ci) {
		forwardButton.visible = true;
		backButton.visible = true;

		ci.cancel();
	}
}
