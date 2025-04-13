package pak.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;

public class SpringMVCDispethercServletInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    // здесь нужен класс с настройками hibernate или другой класс с настройками БД
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {HibernateConfig.class};
    }
    // подставляем название конфигурационного класса Spring
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {SpringConfig.class};
    }
    // указываем какие HTTPS запросы пользователя будут посылаться на сервлет Spring
    // при использование "/" все запросы будут посылаться на этот сервлет
    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
    // запускается при старте Spring
    @Override
    public void onStartup(ServletContext aServletContext) throws ServletException {
        super.onStartup(aServletContext);
        // добавляем кастомные фильтр на кодировку и
        // чтение скрытых запросов
        registerCharacterEncodingFilter(aServletContext);
        registerHiddenFieldFilter(aServletContext);
    }
    // добавляем фильтр
    private void registerHiddenFieldFilter(ServletContext aContext) {
        aContext.addFilter("hiddenHttpMethodFilter",
                // /* означает что при любом запросе будет обрабатываться данный фильтр
                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null, true, "/*");
    }
    // добавляем фильтр на кодировку UTF-8 для корректного считывания
    // кириллицы
    private void registerCharacterEncodingFilter(ServletContext aContext) {
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);

        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        FilterRegistration.Dynamic characterEncoding = aContext.addFilter("characterEncoding", characterEncodingFilter);
        characterEncoding.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
    }
}
