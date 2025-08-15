package me.devjg.bookutils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.widget.PageTurnWidget;

@Mixin(BookEditScreen.class)
abstract class EditableBookArrowsMixin {
	@Shadow private int currentPage;
	@Shadow private PageTurnWidget previousPageButton;

	@Shadow abstract int countPages();
	@Shadow abstract void updatePreviousPageButtonVisibility();
	@Shadow abstract void updatePage();
	@Shadow abstract void appendNewPage();

	@Inject(method = "openPreviousPage", at = @At("HEAD"), cancellable = true)
	private void onOpenPreviousPage(CallbackInfo ci) {
		if (currentPage == 0)
			appendNewPage();

		int maxValue = countPages();
		currentPage = (currentPage - 1 + maxValue) % maxValue;

		updatePage();
		updatePreviousPageButtonVisibility();

		ci.cancel();
	}

	@Inject(method = "updatePreviousPageButtonVisibility", at = @At("HEAD"), cancellable = true)
	private void onUpdateButtons(CallbackInfo ci) {
		previousPageButton.visible = true;
		ci.cancel();
	}
}
