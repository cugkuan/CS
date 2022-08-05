package com.brightk.cs.core;

import androidx.annotation.NonNull;

public interface CsService {

    UriRespond call(@NonNull UriRequest uriRequest);
}
