package org.royllo.explorer.web.util.advice;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.util.parameters.RoylloExplorerParameters;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.API_BASE_URL_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.WEB_BASE_URL_ATTRIBUTE;

/**
 * Controller advice used to distribute configuration to all controllers.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class ConfigurationControllerAdvice {

    /** Royllo explorer parameters. */
    private final RoylloExplorerParameters roylloExplorerParameters;

    @ModelAttribute
    public final void handleRequest(final Model model) {

        // Set the base url for web and API.
        model.addAttribute(API_BASE_URL_ATTRIBUTE, roylloExplorerParameters.getApi().getBaseUrl());
        model.addAttribute(WEB_BASE_URL_ATTRIBUTE, roylloExplorerParameters.getWeb().getBaseUrl());

    }

}
