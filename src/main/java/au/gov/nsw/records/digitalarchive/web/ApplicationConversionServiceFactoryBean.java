package au.gov.nsw.records.digitalarchive.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

import au.gov.nsw.records.digitalarchive.model.Pathway;

/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}
	
	public Converter<Pathway, String> getPathwayToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<au.gov.nsw.records.digitalarchive.model.Pathway, java.lang.String>() {
			public String convert(Pathway pathway) {
				return String.format("%s %s -> %s",pathway.getId(), pathway.getInput_format_desc(), pathway.getTarget_format_desc());
			}
		};
	}
}
