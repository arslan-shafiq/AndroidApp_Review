package de.heuremo.commons.common.api;

import javax.inject.Singleton;

import dagger.Component;
import de.heuremo.steelmeasureapp.components.change_password.ChangePasswordFragment;
import de.heuremo.steelmeasureapp.components.login.LoginFragment;
import de.heuremo.steelmeasureapp.components.login.Registration.RegistrationFormFragment;
import de.heuremo.steelmeasureapp.components.makeorder.MakeOrderFragment;
import de.heuremo.steelmeasureapp.components.order_history.PickupOrdersFragment;

@Singleton
@Component(modules = {CommonApiModule.class})
public interface CommonApiComponent {
    void inject(LoginFragment fragment);

    void inject(RegistrationFormFragment fragment);

    void inject(MakeOrderFragment fragment);

    void inject(PickupOrdersFragment fragment);

    void inject(ChangePasswordFragment fragment);
}
