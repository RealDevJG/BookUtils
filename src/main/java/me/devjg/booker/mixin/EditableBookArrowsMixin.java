package me.devjg.booker.mixin;

import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BookEditScreen.class)
abstract class EditableBookArrowsMixin {
	@Shadow private int currentPage;
	@Shadow private PageButton backButton;

	@Shadow protected abstract int getNumPages();
	@Shadow protected abstract void updateButtonVisibility();
	@Shadow protected abstract void updatePageContent();
	@Shadow protected abstract void appendPageToBook();

	@Inject(method = "pageBack", at = @At("HEAD"), cancellable = true)
	private void onOpenPreviousPage(CallbackInfo ci) {
		if (currentPage == 0)
			appendPageToBook();

		int maxValue = getNumPages();
		currentPage = (currentPage - 1 + maxValue) % maxValue;

		updatePageContent();
		updateButtonVisibility();

		ci.cancel();
	}

	@Inject(method = "updateButtonVisibility", at = @At("HEAD"), cancellable = true)
	private void onUpdateButtons(CallbackInfo ci) {
		backButton.visible = true;
		ci.cancel();
	}
}
