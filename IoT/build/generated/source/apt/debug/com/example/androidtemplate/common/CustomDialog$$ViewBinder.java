// Generated code from Butter Knife. Do not modify!
package com.example.androidtemplate.common;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CustomDialog$$ViewBinder<T extends com.example.androidtemplate.common.CustomDialog> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361826, "field 'hintMsgTv'");
    target.hintMsgTv = finder.castView(view, 2131361826, "field 'hintMsgTv'");
    view = finder.findRequiredView(source, 2131361827, "field 'okBtn'");
    target.okBtn = finder.castView(view, 2131361827, "field 'okBtn'");
    view = finder.findRequiredView(source, 2131361828, "field 'cancelBtn' and method 'onClick'");
    target.cancelBtn = finder.castView(view, 2131361828, "field 'cancelBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361802, "field 'passwordEt'");
    target.passwordEt = finder.castView(view, 2131361802, "field 'passwordEt'");
    view = finder.findRequiredView(source, 2131361796, "field 'contentLl'");
    target.contentLl = finder.castView(view, 2131361796, "field 'contentLl'");
  }

  @Override public void unbind(T target) {
    target.hintMsgTv = null;
    target.okBtn = null;
    target.cancelBtn = null;
    target.passwordEt = null;
    target.contentLl = null;
  }
}
