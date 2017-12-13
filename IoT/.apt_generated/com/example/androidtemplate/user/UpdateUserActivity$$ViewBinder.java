// Generated code from Butter Knife. Do not modify!
package com.example.androidtemplate.user;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class UpdateUserActivity$$ViewBinder<T extends com.example.androidtemplate.user.UpdateUserActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361796, "field 'contentLl'");
    target.contentLl = finder.castView(view, 2131361796, "field 'contentLl'");
    view = finder.findRequiredView(source, 2131361793, "field 'leftTv' and method 'onClick'");
    target.leftTv = finder.castView(view, 2131361793, "field 'leftTv'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361821, "field 'sexSp'");
    target.sexSp = finder.castView(view, 2131361821, "field 'sexSp'");
    view = finder.findRequiredView(source, 2131361819, "field 'weChatEt'");
    target.weChatEt = finder.castView(view, 2131361819, "field 'weChatEt'");
    view = finder.findRequiredView(source, 2131361792, "field 'titleLl'");
    target.titleLl = finder.castView(view, 2131361792, "field 'titleLl'");
    view = finder.findRequiredView(source, 2131361812, "field 'imgIv'");
    target.imgIv = finder.castView(view, 2131361812, "field 'imgIv'");
    view = finder.findRequiredView(source, 2131361798, "field 'emailEt'");
    target.emailEt = finder.castView(view, 2131361798, "field 'emailEt'");
    view = finder.findRequiredView(source, 2131361820, "field 'birthEt'");
    target.birthEt = finder.castView(view, 2131361820, "field 'birthEt'");
    view = finder.findRequiredView(source, 2131361817, "field 'telEt'");
    target.telEt = finder.castView(view, 2131361817, "field 'telEt'");
    view = finder.findRequiredView(source, 2131361797, "field 'usernameEt'");
    target.usernameEt = finder.castView(view, 2131361797, "field 'usernameEt'");
    view = finder.findRequiredView(source, 2131361818, "field 'qqEt'");
    target.qqEt = finder.castView(view, 2131361818, "field 'qqEt'");
    view = finder.findRequiredView(source, 2131361794, "field 'titleTv'");
    target.titleTv = finder.castView(view, 2131361794, "field 'titleTv'");
    view = finder.findRequiredView(source, 2131361825, "field 'updateBtn' and method 'onClick'");
    target.updateBtn = finder.castView(view, 2131361825, "field 'updateBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361814, "field 'cameraImgBtn' and method 'onClick'");
    target.cameraImgBtn = finder.castView(view, 2131361814, "field 'cameraImgBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361813, "field 'selectImgBtn' and method 'onClick'");
    target.selectImgBtn = finder.castView(view, 2131361813, "field 'selectImgBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361795, "field 'rightTv'");
    target.rightTv = finder.castView(view, 2131361795, "field 'rightTv'");
  }

  @Override public void unbind(T target) {
    target.contentLl = null;
    target.leftTv = null;
    target.sexSp = null;
    target.weChatEt = null;
    target.titleLl = null;
    target.imgIv = null;
    target.emailEt = null;
    target.birthEt = null;
    target.telEt = null;
    target.usernameEt = null;
    target.qqEt = null;
    target.titleTv = null;
    target.updateBtn = null;
    target.cameraImgBtn = null;
    target.selectImgBtn = null;
    target.rightTv = null;
  }
}
