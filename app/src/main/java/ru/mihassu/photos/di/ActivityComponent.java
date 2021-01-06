package ru.mihassu.photos.di;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Component;

@Component(modules = {ActivityContextModule.class})
@ActivityScope
public interface ActivityComponent {
    AppCompatActivity getActivity();
}
